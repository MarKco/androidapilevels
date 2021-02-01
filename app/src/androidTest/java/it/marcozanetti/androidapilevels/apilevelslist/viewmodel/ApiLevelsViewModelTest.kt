package it.marcozanetti.androidapilevels.apilevelslist.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.marcozanetti.androidapilevels.apilevelslist.view.ApiLevelsFragment
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ApiLevelsViewModelTest {

    @Test
    fun getAPILevels_retrieve_checkNumber() {
        val fragment = ApiLevelsFragment()

        val application = fragment.activity?.application
        if (application != null) {
            val viewModelFactory = ApiLevelsViewModelFactory(application)

            val viewModel = ViewModelProvider(fragment, viewModelFactory).get(ApiLevelsViewModel::class.java)

            val apiLevels = viewModel.getAPILevels()
            assertEquals(apiLevels.size, 18)
        }
    }
}
