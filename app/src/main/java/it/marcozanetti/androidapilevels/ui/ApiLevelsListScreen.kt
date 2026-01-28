package it.marcozanetti.androidapilevels.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel
import it.marcozanetti.androidapilevels.apilevelslist.viewmodel.ApiLevelsViewModel
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ApiLevelsListScreen(
    viewModel: ApiLevelsViewModel = viewModel(),
    onItemClick: (SingleAPILevel) -> Unit = {}
) {
    val apiLevelItems = viewModel.apiLevelItems
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color.White)) {
        items(apiLevelItems) { item ->
            ApiLevelItem(item = item, onClick = { onItemClick(item) })
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
    Surface {
        LazyColumn(modifier = Modifier.fillMaxSize().background(Color.White)) {
            items(items) { item ->
                ApiLevelItem(item = item, onClick = {})
            }
        }
    }
}
