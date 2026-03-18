package it.marcozanetti.androidapilevels.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel
import it.marcozanetti.androidapilevels.apilevelslist.viewmodel.ApiLevelsViewModel
import it.marcozanetti.androidapilevels.apilevelslist.viewmodel.ApiLevelsViewModelFactory
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ApiLevelsListScreen(
    viewModel: ApiLevelsViewModel = viewModel(factory = ApiLevelsViewModelFactory()),
    modifier: Modifier = Modifier,
    onItemClick: (SingleAPILevel) -> Unit = {},
    currentApiLevel: Int = -1
) {
    val uiState by viewModel.uiState.collectAsState()
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        items(uiState.items, key = { it.apiLevelStart }) { item ->
            val isCurrent = item.apiLevelStart % 1 == 0f && item.apiLevelStart.toInt() == currentApiLevel
            ApiLevelItem(item = item, onClick = { onItemClick(item) }, isCurrentLevel = isCurrent)
        }
    }
}

@Preview(showBackground = true, name = "ApiLevelsListScreen Preview")
@Composable
fun ApiLevelsListScreenPreview() {
    val items = listOf(
        SingleAPILevel("Pie", "9.0", "August 6, 2018", true, 28f, 28f, 0),
        SingleAPILevel("Oreo", "8.0", "August 21, 2017", false, 26f, 26f, 0),
        SingleAPILevel("Nougat", "7.0", "August 22, 2016", false, 24f, 24f, 0)
    )
    Surface(color = MaterialTheme.colors.background) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
        ) {
            items(items, key = { it.apiLevelStart }) { item ->
                ApiLevelItem(item = item, onClick = {})
            }
        }
    }
}
