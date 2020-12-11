package it.marcozanetti.androidsdkversions.placeholder

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 */
object SdkVersionContent {

    /**
     * An array of SDK versione.
     */
    val ITEMS: MutableList<SingleSDKversion> = ArrayList()

    /**
     * A map of SKD Versions items, by ID.
     */
    val ITEM_MAP: MutableMap<String, SingleSDKversion> = HashMap()

    init {
        //Popola con tutte le versioni di Android
        val items =
            arrayListOf(
                SingleSDKversion("no official codename", "1.0", "September 23, 2008", false, 1, 1),
                SingleSDKversion("no official codename", "1.1", "February 9, 2009", false, 2, 2),
                SingleSDKversion("Cupcake", "1.5", "April 27, 2009", false, 3, 3),
                SingleSDKversion("Donut", "1.6", "September 15, 2009", false, 4, 4),
                SingleSDKversion("Eclair", "2.0 - 2.1", "October 26, 2009", false, 5, 7),
                SingleSDKversion("Froyo", "2.2 - 2.2.3", "May 20, 2010", false, 8, 8),
                SingleSDKversion("Gingerbread", "2.3 - 2.3.7", "December 6, 2010", false, 9, 10),
                SingleSDKversion("Honeycomb", "3.0 - 3.2.6", "February 22, 2011", false, 11, 13),
                SingleSDKversion("Ice Cream Sandwich", "4.0 - 4.0.4", "October 18, 2011", false, 14, 15),
                SingleSDKversion("Jelly Bean", "4.1 - 4.3.1", "July 9, 2012", false, 16, 18),
                SingleSDKversion("kitKat", "4.4 - 4.4.4", "October 31, 2013", false, 19, 20),
                SingleSDKversion("Lollipop", "5.0 - 5.1.1", "November 12, 2014", false, 21, 22),
                SingleSDKversion("Marshmallow", "6.0 - 6.0.1", "October 5, 2015", false, 23, 23),
                SingleSDKversion("Nougat", "7.0 - 7.1.2", "August 22, 2016", false, 24, 25),
                SingleSDKversion("Oreo", "8.0 - 8.1", "August 21, 2017", true, 26, 27),
                SingleSDKversion("Pie", "9", "August 6, 2018", true, 28, 28),
                SingleSDKversion("Android 10", "10", "September 3, 2019", true, 29, 29),
                SingleSDKversion("Android 11", "11", "September 8, 2020", true, 30, 30),
            )
        items.sortByDescending { list -> list.apiLevelStart }

        addItems(items)
    }

    private fun addItems(arrayList: ArrayList<SingleSDKversion>) {
        for (singleItem in arrayList) {
            addItem(singleItem)
        }
    }

    private fun addItem(item: SingleSDKversion) {
        ITEMS.add(item)
        ITEM_MAP.put(item.codeName, item)
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A placeholder item representing a piece of content.
     */
    data class SingleSDKversion(
        val codeName: String,
        val versionNumber: String,
        val releaseDate: String,
        val supported: Boolean,
        val apiLevelStart: Int,
        val apiLevelEnd: Int) {

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
}