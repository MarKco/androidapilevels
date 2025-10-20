package it.marcozanetti.androidapilevels.apilevelslist.model

/**
 * An item representing a single Android Version
 */
data class SingleAPILevel(
    val codeName: String,
    val versionNumber: String,
    var releaseDate: String,
    var supported: Boolean,
    val apiLevelStart: Int,
    val apiLevelEnd: Int,
    val logoResourceId: Int = 0) {

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
        var stringToPrint = "API $apiLevelStart"
        if (apiLevelEnd > apiLevelStart) {
            stringToPrint += "-$apiLevelEnd"
        }

        return stringToPrint
    }
}
