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
    fun getApiText(): String {
        return "API ${formatApiLevel(apiLevelStart)}" +
            if (apiLevelEnd > apiLevelStart) "-${formatApiLevel(apiLevelEnd)}" else ""
    }

    private fun formatApiLevel(level: Float): String =
        if (level == level.toInt().toFloat()) level.toInt().toString() else level.toString()
}
