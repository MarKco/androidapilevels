package it.marcozanetti.androidapilevels.apilevelslist.model

import androidx.lifecycle.MutableLiveData
import java.lang.Exception

/**
 * Generic repository in order to retrieve the list of API levels.
 * Should a Database be implemented we'd just need to change the
 * implementation of the repository
 */
interface APILevelsRepository {

    val apiLevelsList: MutableLiveData<List<SingleAPILevel>>
    val exceptionsWhileRetrieving: MutableLiveData<Exception>

    fun getAPILevels()  //Doesn't return anything because it loads data in the the apiLevelsList variable

}