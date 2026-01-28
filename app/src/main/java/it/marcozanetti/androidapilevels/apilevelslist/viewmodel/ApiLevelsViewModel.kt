package it.marcozanetti.androidapilevels.apilevelslist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import it.marcozanetti.androidapilevels.apilevelslist.model.DefaultDataProvider
import it.marcozanetti.androidapilevels.apilevelslist.model.APILevelsRepository
import it.marcozanetti.androidapilevels.apilevelslist.model.APILevelsRepositoryImpl
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel class. It retrieves data from the Model class (any class implementing APILevels
 * interface), passes them to the fragment view via the getAPILevels method.
 * Doesn't yet implement LiveData and/or Events
 */
class ApiLevelsViewModel(
    private val repository: APILevelsRepository = APILevelsRepositoryImpl()
) : ViewModel() {
    var apiLevelItems by mutableStateOf<List<SingleAPILevel>>(emptyList())
        private set
    var apiLevelItemsRetrieved by mutableStateOf<List<SingleAPILevel>>(emptyList())
        private set

    var exceptionsWhileRetrieving: Exception? = null
        private set

    var displaySearchView by mutableStateOf(false)
        private set

    /**
     * Retrieves data from web page or database
     */
    fun retrieveApiLevelData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = repository.getAPILevelsCompose()
                apiLevelItemsRetrieved = data
                apiLevelItems = data
            } catch (e: Exception) {
                exceptionsWhileRetrieving = e
                val fallback = DefaultDataProvider.getDefaultData()
                apiLevelItemsRetrieved = fallback
                apiLevelItems = fallback
            }
        }
    }

    fun resetData() {
        apiLevelItems = apiLevelItemsRetrieved
    }

    /**
     * Filters APIlevels list based
     * on the provided query
     */
    fun filterData(query: String) {
        val listOfItems = this.apiLevelItemsRetrieved
        apiLevelItems = if (query.isBlank()) {
            listOfItems
        } else {
            listOfItems.filter {
                it.versionNumber.contains(query, true) ||
                    it.codeName.contains(query, true)      ||
                    it.releaseDate.contains(query, true)   ||
                    it.apiLevelStart.toString().contains(query, true)
            }
        }
    }
}