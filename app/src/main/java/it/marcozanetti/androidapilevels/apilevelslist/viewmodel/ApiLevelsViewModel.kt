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

    /**
     * The core of the app is
     * the list of Api Levels to be
     * displayed in the RecyclerView.
     */
    var apiLevelItems: MutableLiveData<List<SingleAPILevel>> = MutableLiveData<List<SingleAPILevel>>()
    var apiLevelItemsRetrieved: MutableLiveData<List<SingleAPILevel>> = MutableLiveData<List<SingleAPILevel>>()

    private lateinit var apiRepository: APILevelsRepository

    private val observerForApiData = Observer<List<SingleAPILevel>> {
        apiLevelItemsRetrieved.value = it
        apiLevelItems.value = it
    }

    init {
        apiLevelItems.value = emptyList()
        apiLevelItemsRetrieved.value = emptyList()
    }

    var displaySearchView = MutableLiveData<Boolean>()

    /**
     * Retrieves data from web page or database
     */
    fun retrieveApiLevelData() {
        apiRepository = APILevelsRepositoryImpl()
        apiRepository.getAPILevels()
        apiRepository.apiLevelsList.observeForever(observerForApiData)
    }

    /**
     * Clears the Observers that were instantiated
     * with ObserveForever in order to prevent memory leaks
     */
    override fun onCleared() {
        super.onCleared()
        apiRepository.apiLevelsList.removeObserver(observerForApiData)
    }


    fun resetData() {
        apiLevelItems.value = apiLevelItemsRetrieved.value
    }

    /**
     * Filters APIlevels list based
     * on the provided query
     */
    fun filterData(query: String) {
        val listOfItems = this.apiLevelItemsRetrieved.value

        if (listOfItems != null) {
            apiLevelItems.value = listOfItems.filter {
                it.versionNumber.contains(query, true) ||
                    it.codeName.contains(query, true)      ||
                    it.releaseDate.contains(query, true)   ||
                    it.apiLevelStart.toString().contains(query, true)
            }
        }
    }
}