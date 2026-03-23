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

    fun getApiLetter(): String? {
        // Mapping delle API alle lettere principali (aggiornato fino ad Android 14)
        return when (apiLevelStart.toInt()) {
            21 -> "L" // Lollipop
            22 -> "L" // Lollipop MR1
            23 -> "M" // Marshmallow
            24, 25 -> "N" // Nougat
            26, 27 -> "O" // Oreo
            28 -> "P" // Pie
            29 -> "Q" // Q
            30 -> "R" // R
            31, 32 -> "S" // S
            33 -> "T" // Tiramisu
            34 -> "U" // Upside Down Cake
            35 -> "V" // ipotetico futuro
            36 -> "W"
            37 -> "X"
            38 -> "Y"
            39 -> "Z"
            40 -> "AA"
            else -> null
        }
    }
}
