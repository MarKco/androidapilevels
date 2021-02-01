package it.marcozanetti.androidapilevels.apilevelslist.model.network

import retrofit2.Call
import retrofit2.http.GET

/**
 * Needed by retrofit in order to configure
 * the call to be performed on the API.
 * Since we're just scraping a file the "." endpoint
 * is enough, we don't have parameters and the response is
 * a string including the whole page HTML code
 */
interface ApiService {
    @GET(".")
    fun getStringResponse(): Call<String>
}