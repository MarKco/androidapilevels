package it.marcozanetti.androidapilevels.apilevelslist.model.network

import retrofit2.Response
import retrofit2.http.GET

/**
 * Since we're just scraping a file
 * the "." endpoint is enough
 */
interface ApiService {
    @GET(".")
    suspend fun getStringResponse(): Response<String>
}