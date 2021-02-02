package it.marcozanetti.androidapilevels.apilevelslist.model

import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import it.marcozanetti.androidapilevels.apilevelslist.model.network.ApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception
import java.net.UnknownHostException
import java.util.ArrayList

class APILevelsRepositoryImpl: APILevelsRepository {

    val WEBSERVICE_BASE_URL = "https://source.android.com/setup/start/build-numbers/?hl=en"

    override val apiLevelsList = MutableLiveData<List<SingleAPILevel>>()
    override val exceptionsWhileRetrieving = MutableLiveData<Exception>()

    override fun getAPILevels() {
        GlobalScope.launch {
            retrieveAPILevelsFromWeb()
        }
    }

    fun retrieveAPILevelsFromWeb() {
        // Building retrofit instance for HTML page retrieval
        val retrofit = Retrofit.Builder()
            .baseUrl(WEBSERVICE_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        // Very simple API service for retrieving single web page
        val apiService = retrofit.create(ApiService::class.java)

        // Performs call in order to retrieve HTML page
        val call: Call<String> = apiService.getStringResponse()

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    var responseString = response.body()
                    val doc: Document =
                        Jsoup.parse(responseString) // Parsing HTML page with Jsoup https://jsoup.org/

                    // Fetching all the tables in the page
                    val tables: Elements = doc.getElementsByTag("table")

                    var retrievedApiLevels = ArrayList<SingleAPILevel>()

                    for (table in tables) {
                        // The table we want is the one that starts with the first column labeled "Codename"
                        // (not the best check in the world, we could surely do better)
                        if (table.children()[0].child(0).child(0).text()
                                .equals("Codename")
                        ) {
                            // We found the right table!
                            // Let's prepare the list of APILevels to pass to the ViewModel

                            //We cycle through all table rows except the header
                            for (singleApiLevelRetrieved in table.children()[1].children()) {
                                with(singleApiLevelRetrieved) {
                                    // Parsing HTML elements in a SingleApiLevel object
                                    val singleAPILevelToReturn = SingleAPILevel(
                                        versionNumber = children()[1].text(),
                                        supported = true,
                                        releaseDate = "",
                                        codeName = children()[0].text(),
                                        apiLevelStart = children()[2].text()
                                            .substringBefore(",").substringAfter("level ")
                                            .toInt(),
                                        apiLevelEnd = children()[2].text()
                                            .substringBefore(",").substringAfter("level ")
                                            .toInt(),
                                    )
                                    retrievedApiLevels.add(singleAPILevelToReturn)
                                }
                            }
                        }
                    }
                    apiLevelsList.postValue(mergeRetrievedListWithDefaultData(retrievedApiLevels,
                        DefaultDataProvider.getDefaultData()))
                }
                else {
                    exceptionsWhileRetrieving.postValue(Exception(response.message()))
                    apiLevelsList.postValue(DefaultDataProvider.getDefaultData())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                exceptionsWhileRetrieving.postValue(t as Exception)
                apiLevelsList.postValue(DefaultDataProvider.getDefaultData())
            }
        })
    }

    /**
     * Google official page doesn't provide much information.
     * Here we merge those data with default data included in app
     */
    fun mergeRetrievedListWithDefaultData(retrievedList: ArrayList<SingleAPILevel>,
                                          defaultList: List<SingleAPILevel>): ArrayList<SingleAPILevel> {

        for(singleApi in retrievedList) {
            val correspondingItems = defaultList.filter{ it.versionNumber == singleApi.versionNumber }
            if(correspondingItems.isNotEmpty()) {
                with(correspondingItems.first()) {
                    singleApi.releaseDate = this.releaseDate
                    singleApi.supported = this.supported
                }
            }
        }

        return retrievedList
    }

}