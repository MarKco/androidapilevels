package it.marcozanetti.androidapilevels.apilevelslist.model

import androidx.lifecycle.MutableLiveData

interface APILevelsRepository {

    val apiLevelsList: MutableLiveData<List<SingleAPILevel>>

    fun getAPILevels()

}