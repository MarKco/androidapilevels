package it.marcozanetti.androidapilevels.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ApiLevelItem(item: SingleAPILevel, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .width(100.dp)
                    .padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.versionNumber,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = item.getApiText(),
                    style = MaterialTheme.typography.body2
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            if (item.logoResourceId != 0) {
                Image(
                    painter = painterResource(id = item.logoResourceId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(62.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.codeName,
                    style = MaterialTheme.typography.body1
                )
                if (item.releaseDate.isNotEmpty()) {
                    Text(
                        text = item.releaseDate,
                        style = MaterialTheme.typography.body2
                    )
                }
                Text(
                    text = item.apiName ?: "",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "ApiLevelItem Preview")
@Composable
fun ApiLevelItemPreview() {
    ApiLevelItem(
        item = SingleAPILevel(
            codeName = "Pie",
            versionNumber = "9.0",
            releaseDate = "August 6, 2018",
            supported = true,
            apiLevelStart = 28f,
            apiLevelEnd = 28f,
            logoResourceId = 0 // Sostituire con un drawable valido se necessario
        ),
        onClick = {}
    )
}
