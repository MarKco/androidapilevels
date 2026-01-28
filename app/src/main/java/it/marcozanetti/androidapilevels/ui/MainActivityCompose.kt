package it.marcozanetti.androidapilevels.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import it.marcozanetti.androidapilevels.apilevelslist.viewmodel.ApiLevelsViewModel
import androidx.compose.ui.Alignment

class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val viewModel: ApiLevelsViewModel = viewModel()
    val apiLevelItems = viewModel.apiLevelItems
    val isLoading = apiLevelItems.isEmpty()

    LaunchedEffect(Unit) {
        viewModel.retrieveApiLevelData()
    }

    Scaffold(
        content = { padding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    ApiLevelsListScreen(viewModel = viewModel)
                }
            }
        }
    )
}
