package it.marcozanetti.androidapilevels.apilevelslist.model

/**
 * An item representing a single Android Version
 */
data class SingleAPILevel(
    val codeName: String,
    val versionNumber: String,
    var releaseDate: String,
    var supported: Boolean,
    val apiLevelStart: Float,
    val apiLevelEnd: Float,
    var logoResourceId: Int = 0) {

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
