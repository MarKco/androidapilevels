package it.marcozanetti.androidapilevels.apilevelslist.model.network

import retrofit2.Call
import retrofit2.http.GET

/**
 * Since we're just scraping a file
 * the "." endpoint is enough
 */
interface ApiService {
    @GET(".")
    fun getStringResponse(): Call<String>
}