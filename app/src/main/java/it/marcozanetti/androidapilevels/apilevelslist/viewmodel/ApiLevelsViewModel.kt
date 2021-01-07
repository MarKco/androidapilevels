package it.marcozanetti.androidapilevels.apilevelslist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import it.marcozanetti.androidapilevels.apilevelslist.model.APILevels
import it.marcozanetti.androidapilevels.apilevelslist.model.APILevelsContent
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAndroidVersion

class ApiLevelsViewModel(application: Application): AndroidViewModel(application) {

    private var apiLevelItems : MutableList<SingleAndroidVersion> //The list of API levels
                                                                           //to be displayed

    private var apiLevelItemsProvider: APILevels   //Generic (interface) element providing
                                                            //the list of API levels

    init {
        apiLevelItemsProvider = APILevelsContent
        apiLevelItems = apiLevelItemsProvider.getAPILevels()
    }

    fun getAPILevels(): MutableList<SingleAndroidVersion> {
        return apiLevelItems
    }

}