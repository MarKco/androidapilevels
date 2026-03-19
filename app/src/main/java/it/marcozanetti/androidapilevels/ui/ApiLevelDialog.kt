package it.marcozanetti.androidapilevels.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.marcozanetti.androidapilevels.R
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel

@Composable
fun ApiLevelDialog(
    apiLevel: SingleAPILevel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.widthIn(min = 320.dp, max = 480.dp),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                // Icona rimossa: mostra solo il titolo
                Text(
                    text = stringResource(id = R.string.api_info_title),
                    style = MaterialTheme.typography.h6
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildString {
                        append(stringResource(id = R.string.android_version))
                        append(": ")
                        append(apiLevel.codeName)
                        append("\n")
                        append(stringResource(id = R.string.version_number))
                        append(": ")
                        append(apiLevel.versionNumber)
                        append("\n")
                        append(stringResource(id = R.string.api_level))
                        append(": ")
                        append(apiLevel.getApiText())
                        append("\n")
                        append(stringResource(id = R.string.release_date))
                        append(": ")
                        append(apiLevel.releaseDate)
                        apiLevel.apiName?.let {
                            append("\n")
                            append(stringResource(id = R.string.api_name_label))
                            append(it)
                        }
                    },
                    style = MaterialTheme.typography.body1
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "OK")
            }
        }
    )
}
