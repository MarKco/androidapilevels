package it.marcozanetti.androidapilevels.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.marcozanetti.androidapilevels.R
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel

@Composable
fun ApiLevelItem(
    item: SingleAPILevel,
    onClick: () -> Unit,
    isCurrentLevel: Boolean = false
) {
    val statusText = if (item.supported) stringResource(id = R.string.status_supported) else stringResource(id = R.string.status_no_updates)
    val logoSize = 48.dp
    // Highlight color: more vivid for current API level, especially in dark mode
    val highlightColor = lerp(
        MaterialTheme.colors.surface,
        MaterialTheme.colors.secondary,
        if (isCurrentLevel && MaterialTheme.colors.isLight.not()) 0.5f else 0.35f
    )
    val backgroundColor = if (isCurrentLevel) highlightColor else MaterialTheme.colors.surface
    val contentColor = MaterialTheme.colors.onSurface
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 2.dp,
        backgroundColor = backgroundColor,
        border = null, // Nessun bordo
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable { onClick() }
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(logoSize),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.logoResourceId != 0) {
                        Image(
                            painter = painterResource(id = item.logoResourceId),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text(
                            text = item.versionNumber.takeIf { it.isNotEmpty() } ?: "?",
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.codeName,
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Card(
                            shape = RoundedCornerShape(50),
                            backgroundColor = if (item.supported) {
                                MaterialTheme.colors.primary.copy(alpha = 0.14f)
                            } else {
                                MaterialTheme.colors.onSurface.copy(alpha = 0.10f)
                            },
                            elevation = 0.dp
                        ) {
                            Text(
                                text = statusText,
                                style = MaterialTheme.typography.caption,
                                color = if (item.supported) {
                                    if (MaterialTheme.colors.isLight.not()) Color(0xFFFFD600) else MaterialTheme.colors.primary
                                } else MaterialTheme.colors.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Text(
                        text = stringResource(id = R.string.item_info, item.versionNumber, item.getApiText()),
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Medium
                    )

                    if (item.releaseDate.isNotEmpty()) {
                        Text(
                            text = item.releaseDate,
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.75f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    item.apiName?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.75f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "ApiLevelItem Preview")
@Composable
fun ApiLevelItemPreview() {
    ApiLevelItem(
        item = SingleAPILevel(
            codeName = stringResource(id = R.string.pie),
            versionNumber = stringResource(id = R.string.august_6_2018),
            releaseDate = stringResource(id = R.string.august_6_2018),
            supported = true,
            apiLevelStart = 28f,
            apiLevelEnd = 28f,
            logoResourceId = 0 // Sostituire con un drawable valido se necessario
        ),
        onClick = {}
    )
}
