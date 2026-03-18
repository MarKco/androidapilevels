package it.marcozanetti.androidapilevels.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.marcozanetti.androidapilevels.apilevelslist.viewmodel.ApiLevelsViewModel
import androidx.compose.ui.graphics.Color

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
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Android API Levels", color = Color.White) },
                backgroundColor = Color(0xFF222222),
                contentColor = Color.White,
                actions = {
                    // Spinner visible while the network fetch is in flight.
                    // The list is always shown (populated with local data) so
                    // the user is never staring at a blank screen.
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(24.dp)
                        )
                    }
                }
            )
        },
        content = { padding ->
            ApiLevelsListScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(padding)
            )
        }
    )
}


