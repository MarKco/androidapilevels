package it.marcozanetti.androidapilevels.apilevelslist.model

import androidx.lifecycle.MutableLiveData
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
import java.net.UnknownHostException
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * Pretty much everything is in the companion object, which implements
 * the APILevels interface in order to provide data to the ViewModel.
 *
 * In the MVVM pattern here is the MODEL
 */
class APILevelsRepositoryImpl {

    companion object : APILevelsRepository {

        /**
         * An array of SDK version.
         */
        override val apiLevelsList = MutableLiveData<List<SingleAPILevel>>()

        /**
         * A map of SKD Versions items, by ID.
         */
        lateinit var apiLevelsListByCode: Map<String, SingleAPILevel>

        fun populateListWithDefaultData() {
            //Populates list with Android versions up to Android 11 - updated on Feb 1 2021
            val items =
                arrayListOf(
                    SingleAPILevel(
                        "no official codename",
                        "1.0",
                        "September 23, 2008",
                        false,
                        1,
                        1
                    ),
                    SingleAPILevel("no official codename", "1.1", "February 9, 2009", false, 2, 2),
                    SingleAPILevel("Cupcake", "1.5", "April 27, 2009", false, 3, 3),
                    SingleAPILevel("Donut", "1.6", "September 15, 2009", false, 4, 4),
                    SingleAPILevel("Eclair", "2.0 - 2.1", "October 26, 2009", false, 5, 7),
                    SingleAPILevel("Froyo", "2.2 - 2.2.3", "May 20, 2010", false, 8, 8),
                    SingleAPILevel("Gingerbread", "2.3 - 2.3.7", "December 6, 2010", false, 9, 10),
                    SingleAPILevel("Honeycomb", "3.0 - 3.2.6", "February 22, 2011", false, 11, 13),
                    SingleAPILevel(
                        "Ice Cream Sandwich",
                        "4.0 - 4.0.4",
                        "October 18, 2011",
                        false,
                        14,
                        15
                    ),
                    SingleAPILevel("Jelly Bean", "4.1 - 4.3.1", "July 9, 2012", false, 16, 18),
                    SingleAPILevel("kitKat", "4.4 - 4.4.4", "October 31, 2013", false, 19, 20),
                    SingleAPILevel("Lollipop", "5.0 - 5.1.1", "November 12, 2014", false, 21, 22),
                    SingleAPILevel("Marshmallow", "6.0 - 6.0.1", "October 5, 2015", false, 23, 23),
                    SingleAPILevel("Nougat", "7.0 - 7.1.2", "August 22, 2016", false, 24, 25),
                    SingleAPILevel("Oreo", "8.0 - 8.1", "August 21, 2017", true, 26, 27),
                    SingleAPILevel("Pie", "9", "August 6, 2018", true, 28, 28),
                    SingleAPILevel("Android 10", "10", "September 3, 2019", true, 29, 29),
                    SingleAPILevel("Android 11", "11", "September 8, 2020", true, 30, 30),
                )
            items.sortByDescending { list -> list.apiLevelStart }

            apiLevelsList.value = items
            apiLevelsListByCode = items.associateBy({ it.codeName }, { it })
        }

        override fun getAPILevels() {
            GlobalScope.launch {
                retrieveAPILevelsFromWeb()
            }
        }

        fun retrieveAPILevelsFromWeb() {
            // Hardcoded constant URL of remote page for fetching API Levels
            val WEBSERVICE_BASE_URL = "https://source.android.com/setup/start/build-numbers/?hl=en"

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

                        for (table in tables) {
                            // The table we want is the one that starts with the first column labeled "Codename"
                            // (not the best check in the world, we could surely do better)
                            if (table.children()[0].child(0).child(0).text()
                                    .equals("Codename")
                            ) {//This is what we expect in the table
                                // We found the right table!
                                // Let's prepare the list of APILevels to pass to the ViewModel
                                var temporaryListOfApiLevelsToReturn = ArrayList<SingleAPILevel>()

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

                                        temporaryListOfApiLevelsToReturn.add(singleAPILevelToReturn)
                                    }
                                }

                                apiLevelsList.postValue(temporaryListOfApiLevelsToReturn)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    //Log.e("Error", t.message!!)
                    if (t is UnknownHostException) {
                        apiLevelsList.postValue(ArrayList())
                    }
                }
            })
        }
    }
}