package it.marcozanetti.androidapilevels.apilevelslist.model

import it.marcozanetti.androidapilevels.apilevelslist.model.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class APILevelsRepositoryImpl : APILevelsRepository {

    companion object {
        private const val WEBSERVICE_BASE_URL =
            "https://source.android.com/setup/start/build-numbers/?hl=en"

        // Singleton: built once and reused across all calls
        private val apiService: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(WEBSERVICE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

    /**
     * Fetches the API levels from the web page, merges them with local defaults
     * and returns the result. Throws on network/parse error so the caller can
     * decide how to handle the fallback.
     */
    override suspend fun getAPILevelsCompose(): List<SingleAPILevel> = withContext(Dispatchers.IO) {
        val response = apiService.getStringResponse()
        check(response.isSuccessful) { "HTTP error ${response.code()}" }

        val doc = Jsoup.parse(response.body().orEmpty())
        val retrievedApiLevels = mutableListOf<SingleAPILevel>()

        for (table in doc.getElementsByTag("table")) {
            if (table.children()[0].child(0).child(0).text() == "Codename") {
                for (row in table.children()[1].children()) {
                    val apiLevel = row.children()[2].text()
                        .substringBefore(",")
                        .substringAfter("level ")
                        .toFloatOrNull() ?: continue
                    retrievedApiLevels.add(
                        SingleAPILevel(
                            codeName = row.children()[0].text(),
                            versionNumber = row.children()[1].text(),
                            releaseDate = "",
                            supported = true,
                            apiLevelStart = apiLevel,
                            apiLevelEnd = apiLevel,
                        )
                    )
                }
            }
        }

        check(retrievedApiLevels.isNotEmpty()) { "No API levels found in page — HTML structure may have changed" }

        mergeWithDefaultData(retrievedApiLevels, DefaultDataProvider.data)
    }

    /**
     * Enriches the network list with release dates, logos and support flags
     * from local defaults. Uses a Map for O(n+m) complexity.
     */
    private fun mergeWithDefaultData(
        retrieved: List<SingleAPILevel>,
        defaults: List<SingleAPILevel>
    ): List<SingleAPILevel> {
        val defaultsByVersion = defaults.associateBy { it.versionNumber }
        return retrieved.map { item ->
            val match = defaultsByVersion[item.versionNumber]
            if (match != null) {
                item.copy(
                    releaseDate = match.releaseDate,
                    supported = match.supported,
                    logoResourceId = match.logoResourceId,
                    apiName = match.apiName
                )
            } else {
                item
            }
        }
    }
}

