package it.marcozanetti.androidapilevels.apilevelslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.marcozanetti.androidapilevels.apilevelslist.model.DefaultDataProvider
import it.marcozanetti.androidapilevels.apilevelslist.model.APILevelsRepository
import it.marcozanetti.androidapilevels.apilevelslist.model.APILevelsRepositoryImpl
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI state for the API levels screen.
 *
 * [isLoading] is true while the network request is in flight — the list
 * stays visible (populated with default data) during this time.
 * [hasNetworkError] is true when the fetch failed and default data is shown.
 */
data class ApiLevelsUiState(
    val isLoading: Boolean = true,
    val items: List<SingleAPILevel> = DefaultDataProvider.data,
    val hasNetworkError: Boolean = false
)

/**
 * ViewModel for the API levels screen.
 * Exposes a single [uiState] StateFlow that the UI collects.
 */
class ApiLevelsViewModel(
    private val repository: APILevelsRepository = APILevelsRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApiLevelsUiState())
    val uiState: StateFlow<ApiLevelsUiState> = _uiState.asStateFlow()

    // Full unfiltered list used to reset/re-filter after a search
    private var allItems: List<SingleAPILevel> = DefaultDataProvider.data

    init {
        retrieveApiLevelData()
    }

    /**
     * Fetches data from the network. Falls back to local default data on error.
     */
    fun retrieveApiLevelData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasNetworkError = false) }
            try {
                val data = repository.getAPILevelsCompose()
                allItems = data
                _uiState.update { it.copy(isLoading = false, items = data) }
            } catch (e: Exception) {
                allItems = DefaultDataProvider.data
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        items = DefaultDataProvider.data,
                        hasNetworkError = true
                    )
                }
            }
        }
    }

    /** Resets the list to the full unfiltered result. */
    fun resetData() {
        _uiState.update { it.copy(items = allItems) }
    }

    /** Filters the list by [query] across all fields of SingleAPILevel. */
    fun filterData(query: String) {
        val filtered = if (query.isBlank()) {
            allItems
        } else {
            allItems.filter {
                it.versionNumber.contains(query, ignoreCase = true) ||
                    it.codeName.contains(query, ignoreCase = true) ||
                    it.releaseDate.contains(query, ignoreCase = true) ||
                    it.apiLevelStart.toString().contains(query, ignoreCase = true) ||
                    it.apiLevelEnd.toString().contains(query, ignoreCase = true) ||
                    (it.apiName?.contains(query, ignoreCase = true) ?: false)
            }
        }
        _uiState.update { it.copy(items = filtered) }
    }
}
