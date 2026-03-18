package it.marcozanetti.androidapilevels.apilevelslist.model

import androidx.compose.runtime.Immutable

/**
 * An item representing a single Android Version.
 *
 * [@Immutable] guarantees the Compose compiler that every property is truly
 * immutable, enabling full skippability of any composable that receives this
 * type as a parameter.
 */
@Immutable
data class SingleAPILevel(
    val codeName: String,
    val versionNumber: String,
    val releaseDate: String,
    val supported: Boolean,
    val apiLevelStart: Float,
    val apiLevelEnd: Float,
    val logoResourceId: Int = 0,
    val apiName: String? = null
) {

    override fun toString(): String {
        var stringToPrint = "Android $codeName version $versionNumber released on $releaseDate with API level $apiLevelStart"
        if (apiLevelEnd > apiLevelStart) {
            stringToPrint += "-$apiLevelEnd"
        }
        if(!supported) stringToPrint += " not"
        stringToPrint += " supported for security updates"

        return stringToPrint
    }

    fun getApiText() : String {
        var stringToPrint = "API ${formatApiLevel(apiLevelStart)}"
        if (apiLevelEnd > apiLevelStart) {
            stringToPrint += "-${formatApiLevel(apiLevelEnd)}"
        }

        return stringToPrint
    }

    // Formats the API level as int if it doesn't have any subversion (es. API 16), keeps the subversion otherwise (es. 16.1)
    private fun formatApiLevel(level: Float): String =
        if (level == level.toInt().toFloat()) level.toInt().toString() else level.toString()
}
