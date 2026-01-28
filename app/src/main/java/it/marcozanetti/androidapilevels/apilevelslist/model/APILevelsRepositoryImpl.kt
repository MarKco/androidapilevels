package it.marcozanetti.androidapilevels.apilevelslist.model

import it.marcozanetti.androidapilevels.apilevelslist.model.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class APILevelsRepositoryImpl : APILevelsRepository {
    private val WEBSERVICE_BASE_URL = "https://source.android.com/setup/start/build-numbers/?hl=en"

    override suspend fun getAPILevelsCompose(): List<SingleAPILevel> = withContext(Dispatchers.IO) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(WEBSERVICE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
            val apiService = retrofit.create(ApiService::class.java)
            val response = apiService.getStringResponse().execute()
            if (response.isSuccessful) {
                val responseString = response.body().orEmpty()
                val doc = Jsoup.parse(responseString)
                val tables = doc.getElementsByTag("table")
                val retrievedApiLevels = ArrayList<SingleAPILevel>()
                for (table in tables) {
                    if (table.children()[0].child(0).child(0).text() == "Codename") {
                        for (singleApiLevelRetrieved in table.children()[1].children()) {
                            with(singleApiLevelRetrieved) {
                                val singleAPILevelToReturn = SingleAPILevel(
                                    versionNumber = children()[1].text(),
                                    supported = true,
                                    releaseDate = "",
                                    codeName = children()[0].text(),
                                    apiLevelStart = children()[2].text().substringBefore(",").substringAfter("level ").toFloat(),
                                    apiLevelEnd = children()[2].text().substringBefore(",").substringAfter("level ").toFloat(),
                                )
                                retrievedApiLevels.add(singleAPILevelToReturn)
                            }
                        }
                    }
                }
                return@withContext DefaultDataProvider.getDefaultData().let { defaultList ->
                    mergeRetrievedListWithDefaultData(retrievedApiLevels, defaultList)
                }
            } else {
                return@withContext DefaultDataProvider.getDefaultData()
            }
        } catch (e: Exception) {
            return@withContext DefaultDataProvider.getDefaultData()
        }
    }

    private fun mergeRetrievedListWithDefaultData(retrievedList: ArrayList<SingleAPILevel>,
                                                  defaultList: List<SingleAPILevel>): ArrayList<SingleAPILevel> {
        for (singleApi in retrievedList) {
            val correspondingItems = defaultList.filter { it.versionNumber == singleApi.versionNumber }
            if (correspondingItems.isNotEmpty()) {
                with(correspondingItems.first()) {
                    singleApi.releaseDate = this.releaseDate
                    singleApi.supported = this.supported
                    singleApi.logoResourceId = this.logoResourceId
                    singleApi.apiName = this.apiName
                }
            }
        }
        return retrievedList
    }

}