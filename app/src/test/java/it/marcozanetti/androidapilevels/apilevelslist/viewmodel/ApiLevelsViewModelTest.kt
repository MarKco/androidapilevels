package it.marcozanetti.androidapilevels.apilevelslist.viewmodel

import it.marcozanetti.androidapilevels.apilevelslist.model.DefaultDataProvider
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ApiLevelsViewModelTest {
    private lateinit var viewModel: ApiLevelsViewModel

    @Before
    fun setUp() {
        viewModel = ApiLevelsViewModel(DefaultDataProvider.data)
    }

    @Test
    fun initialList_isDefaultData() {
        val items = viewModel.uiState.value.items
        assertEquals(DefaultDataProvider.data.size, items.size)
        assertTrue(items.containsAll(DefaultDataProvider.data))
    }

    @Test
    fun filterData_filtersByCodeName() {
        viewModel.filterData("Pie")
        val filtered = viewModel.uiState.value.items
        assertTrue(filtered.all { it.codeName.contains("Pie", ignoreCase = true) ||
            it.versionNumber.contains("Pie", ignoreCase = true) ||
            it.releaseDate.contains("Pie", ignoreCase = true) ||
            it.apiName?.contains("Pie", ignoreCase = true) == true ||
            it.apiLevelStart.toString().contains("Pie", ignoreCase = true) ||
            it.apiLevelEnd.toString().contains("Pie", ignoreCase = true)
        })
    }

    @Test
    fun filterData_emptyQuery_showsAll() {
        viewModel.filterData("")
        val filtered = viewModel.uiState.value.items
        assertEquals(DefaultDataProvider.data.size, filtered.size)
    }

    @Test
    fun resetData_restoresAllItems() {
        viewModel.filterData("Pie")
        viewModel.resetData()
        val items = viewModel.uiState.value.items
        assertEquals(DefaultDataProvider.data.size, items.size)
    }
}
