package it.marcozanetti.androidapilevels.apilevelslist.model

import it.marcozanetti.androidapilevels.R

/**
 * The only purpose of this class is to provide
 * a set of default data for the app in case
 * connection is missing
 */
class DefaultDataProvider() {
    companion object {
        fun getDefaultData(): List<SingleAPILevel> {
            val items =
                arrayListOf(
                    SingleAPILevel("no official codename", "1.0", "September 23, 2008", false, 1f, 1f, R.drawable.ic_android_1_0),
                    SingleAPILevel("no official codename", "1.1", "February 9, 2009", false, 2f, 2f, R.drawable.ic_android_1_0),
                    SingleAPILevel("Cupcake", "1.5", "April 27, 2009", false, 3f, 3f, R.drawable.ic_android_cupcake),
                    SingleAPILevel("Donut", "1.6", "September 15, 2009", false, 4f, 4f, R.drawable.ic_android_donut),
                    SingleAPILevel("Eclair", "2.0", "October 26, 2009", false, 5f, 5f, R.drawable.ic_android_eclair),
                    SingleAPILevel("Eclair", "2.0.1", "December 3, 2009", false, 6f, 6f, R.drawable.ic_android_eclair),
                    SingleAPILevel("Eclair", "2.1", "January 12, 2010", false, 7f, 7f, R.drawable.ic_android_eclair),
                    SingleAPILevel("Froyo", "2.2", "May 20, 2010", false, 8f, 8f, R.drawable.ic_android_froyo),
                    SingleAPILevel("Froyo", "2.2.x", "September 21, 2010", false, 8f, 8f, R.drawable.ic_android_froyo),
                    SingleAPILevel("Gingerbread", "2.3 - 2.3.2", "December 6, 2010", false, 9f, 9f, R.drawable.ic_android_gingerbread),
                    SingleAPILevel("Gingerbread", "2.3.3 - 2.3.7", "February 9, 2011", false, 10f, 10f, R.drawable.ic_android_gingerbread),
                    SingleAPILevel("Honeycomb", "3.0", "February 22, 2011", false, 11f, 11f, R.drawable.ic_android_honeycomb),
                    SingleAPILevel("Honeycomb", "3.1", "May 10, 2011", false, 12f, 12f, R.drawable.ic_android_honeycomb),
                    SingleAPILevel("Honeycomb", "3.2.x", "July 15, 2011", false, 13f, 13f, R.drawable.ic_android_honeycomb),
                    SingleAPILevel("Ice Cream Sandwich", "4.0.1 - 4.0.2", "October 18, 2011", false, 14f, 14f, R.drawable.ic_android_ice_cream_sandwich),
                    SingleAPILevel("Ice Cream Sandwich", "4.0.3 - 4.0.4", "December 16, 2011", false, 15f, 15f, R.drawable.ic_android_ice_cream_sandwich),
                    SingleAPILevel("Jelly Bean", "4.1.x", "July 9, 2012", false, 16f, 16f, R.drawable.ic_android_jelly_bean),
                    SingleAPILevel("Jelly Bean", "4.2.x", "November 13, 2012", false, 17f, 17f, R.drawable.ic_android_jelly_bean),
                    SingleAPILevel("Jelly Bean", "4.3.x", "July 24, 2013", false, 18f, 18f, R.drawable.ic_android_jelly_bean),
                    SingleAPILevel("kitKat", "4.4 - 4.4.4", "October 31, 2013", false, 19f, 19f, R.drawable.ic_android_kitkat),
                    SingleAPILevel("Lollipop", "5.0", "November 12, 2014", false, 21f, 21f, R.drawable.ic_android_lollipop),
                    SingleAPILevel("Lollipop", "5.1", "March 9, 2015", false, 22f, 22f, R.drawable.ic_android_lollipop),
                    SingleAPILevel("Marshmallow", "6.0", "October 5, 2015", false, 23f, 23f, R.drawable.ic_android_marshmallow),
                    SingleAPILevel("Nougat", "7.0", "August 22, 2016", false, 24f, 24f, R.drawable.ic_android_nougat),
                    SingleAPILevel("Nougat", "7.1", "October 4, 2016", false, 25f, 25f, R.drawable.ic_android_nougat),
                    SingleAPILevel("Oreo", "8.0.0", "August 21, 2017", false, 26f, 26f, R.drawable.ic_android_oreo),
                    SingleAPILevel("Oreo", "8.1.0", "December 5, 2017", false, 27f, 27f, R.drawable.ic_android_oreo),
                    SingleAPILevel("Pie", "9", "August 6, 2018", false, 28f, 28f, R.drawable.ic_android_pie),
                    SingleAPILevel("Android10", "10", "September 3, 2019", false, 29f, 29f, R.drawable.ic_android_ten),
                    SingleAPILevel("Android11", "11", "September 8, 2020", false, 30f, 30f, R.drawable.ic_android_eleven),
                    SingleAPILevel("Android12", "12", "October 4, 2021", false, 31f, 31f, R.drawable.ic_android_twelve),
                    SingleAPILevel("Android12L", "12L", "March 7, 2022", false, 32f, 32f, R.drawable.ic_android_twelve),
                    SingleAPILevel("Android13", "13", "August 15, 2022", true, 33f, 33f, R.drawable.ic_android_thirteen),
                    SingleAPILevel("Android14", "14", "October 4, 2023", true, 34f, 34f, R.drawable.ic_android_fourteen),
                    SingleAPILevel("Android15", "15", "September 3, 2024", true, 35f, 35f, R.drawable.ic_android_fifteen),
                    SingleAPILevel("Android16", "16", "June 10, 2025", true, 36f, 36f, R.drawable.ic_android_sixteen),
                    SingleAPILevel("Android16 QPR2", "16", "December 12, 2025", true, 36.1f, 36.1f, R.drawable.ic_android_sixteen),
                )

            return items.sortedWith(compareByDescending({it.apiLevelStart}))
        }
    }
}