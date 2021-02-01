package it.marcozanetti.androidapilevels.apilevelslist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.marcozanetti.androidapilevels.apilevelslist.model.APILevels
import it.marcozanetti.androidapilevels.apilevelslist.model.APILevelsContent
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel

/**
 * ViewModel class. It retrieves data from the Model class (any class implementing APILevels
 * interface), passes them to the fragment view via the getAPILevels method.
 * Doesn't yet implement LiveData and/or Events
 */
class ApiLevelsViewModel(application: Application): AndroidViewModel(application) {

    private var apiLevelItems : MutableList<SingleAPILevel> //The list of API levels
                                                            //to be displayed

    private var apiLevelItemsProvider: APILevels   //Generic (interface) element providing
                                                   //the list of API levels

    init {
        apiLevelItemsProvider = APILevelsContent
        apiLevelItems = apiLevelItemsProvider.getAPILevels()
    }

    fun getAPILevels(): MutableList<SingleAPILevel> {
        return apiLevelItems
    }

}