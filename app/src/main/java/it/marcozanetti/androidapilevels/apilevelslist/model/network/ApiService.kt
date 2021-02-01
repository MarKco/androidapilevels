package it.marcozanetti.androidapilevels.apilevelslist.model.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET(".")
    fun getStringResponse(): Call<String>
}