package it.marcozanetti.androidapilevels.apilevelslist.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ApiLevelsViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApiLevelsViewModel::class.java)) {
            return ApiLevelsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}