package it.marcozanetti.androidapilevels.apilevelslist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import it.marcozanetti.androidapilevels.apilevelslist.model.APILevelsRepository
import it.marcozanetti.androidapilevels.apilevelslist.model.APILevelsRepositoryImpl
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel

/**
 * ViewModel class. It retrieves data from the Model class (any class implementing APILevels
 * interface), passes them to the fragment view via the getAPILevels method.
 * Doesn't yet implement LiveData and/or Events
 */
class ApiLevelsViewModel(application: Application): AndroidViewModel(application) {

    var apiLevelItems: MutableLiveData<List<SingleAPILevel>> = MutableLiveData<List<SingleAPILevel>>() //The list of API levels
                                                            //to be displayed

    private lateinit var apiLevelItemsProviderRepository: APILevelsRepository   //Generic (interface) element providing
                                                   //the list of API levels

    private val observerForApiData = Observer<List<SingleAPILevel>>() {
        apiLevelItems.postValue(apiLevelItemsProviderRepository.apiLevelsList.value)
    }

    fun retrieveApiLevelData() {
        apiLevelItemsProviderRepository = APILevelsRepositoryImpl
        apiLevelItemsProviderRepository.getAPILevels()

        apiLevelItemsProviderRepository.apiLevelsList.observeForever(observerForApiData)
    }

    override fun onCleared() {
        super.onCleared()
        apiLevelItemsProviderRepository.apiLevelsList.removeObserver(observerForApiData)
    }
}