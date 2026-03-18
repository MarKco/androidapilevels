package it.marcozanetti.androidapilevels.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.marcozanetti.androidapilevels.apilevelslist.viewmodel.ApiLevelsViewModel
import it.marcozanetti.androidapilevels.apilevelslist.viewmodel.ApiLevelsViewModelFactory
import it.marcozanetti.androidapilevels.ui.theme.AndroidAPILevelsTheme
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidAPILevelsTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen() {
    val viewModel: ApiLevelsViewModel = viewModel(factory = ApiLevelsViewModelFactory())
    val uiState by viewModel.uiState.collectAsState()
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isSearching) {
        if (isSearching) {
            focusRequester.requestFocus()
            keyboardController?.show()
        } else {
            focusManager.clearFocus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearching) {
                        TextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                viewModel.filterData(it)
                            },
                            placeholder = {
                                Text(
                                    "Cerca...",
                                    color = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f)
                                )
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .focusRequester(focusRequester),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = MaterialTheme.colors.onPrimary,
                                unfocusedIndicatorColor = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                                cursorColor = MaterialTheme.colors.onPrimary,
                                textColor = MaterialTheme.colors.onPrimary,
                                placeholderColor = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f)
                            ),
                            maxLines = 1
                        )
                    } else {
                        Text("Android API Levels")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                actions = {
                    if (isSearching) {
                        IconButton(onClick = {
                            isSearching = false
                            searchQuery = ""
                            viewModel.resetData()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Chiudi ricerca")
                        }
                    } else {
                        IconButton(onClick = { isSearching = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Cerca")
                        }
                    }
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(24.dp)
                        )
                    }
                }
            )
        },
        backgroundColor = MaterialTheme.colors.background,
        content = { padding ->
            ApiLevelsListScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(padding)
            )
        }
    )
}
