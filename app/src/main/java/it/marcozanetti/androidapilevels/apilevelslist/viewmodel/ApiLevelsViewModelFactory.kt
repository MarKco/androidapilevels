package it.marcozanetti.androidapilevels.apilevelslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.marcozanetti.androidapilevels.apilevelslist.model.APILevelsRepositoryImpl

class ApiLevelsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApiLevelsViewModel::class.java)) {
            return ApiLevelsViewModel(APILevelsRepositoryImpl()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

