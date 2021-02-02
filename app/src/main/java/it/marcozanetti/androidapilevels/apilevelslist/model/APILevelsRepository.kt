package it.marcozanetti.androidapilevels.apilevelslist.model

import androidx.lifecycle.MutableLiveData
import java.lang.Exception

interface APILevelsRepository {

    val apiLevelsList: MutableLiveData<List<SingleAPILevel>>
    val exceptionsWhileRetrieving: MutableLiveData<Exception>

    fun getAPILevels()

}