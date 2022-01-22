package it.marcozanetti.androidapilevels.apilevelslist.model

import java.util.ArrayList

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
                    SingleAPILevel("no official codename", "1.0", "September 23, 2008", false, 1, 1),
                    SingleAPILevel("no official codename", "1.1", "February 9, 2009", false, 2, 2),
                    SingleAPILevel("Cupcake", "1.5", "April 27, 2009", false, 3, 3),
                    SingleAPILevel("Donut", "1.6", "September 15, 2009", false, 4, 4),
                    SingleAPILevel("Eclair", "2.0", "October 26, 2009", false, 5, 5),
                    SingleAPILevel("Eclair", "2.0.1", "December 3, 2009", false, 6, 6),
                    SingleAPILevel("Eclair", "2.1", "January 12, 2010", false, 7, 7),
                    SingleAPILevel("Froyo", "2.2", "May 20, 2010", false, 8, 8),
                    SingleAPILevel("Froyo", "2.2.x", "September 21, 2010", false, 8, 8),
                    SingleAPILevel("Gingerbread", "2.3 - 2.3.2", "December 6, 2010", false, 9, 9),
                    SingleAPILevel("Gingerbread", "2.3.3 - 2.3.7", "February 9, 2011", false, 10, 10),
                    SingleAPILevel("Honeycomb", "3.0", "February 22, 2011", false, 11, 11),
                    SingleAPILevel("Honeycomb", "3.1", "May 10, 2011", false, 12, 12),
                    SingleAPILevel("Honeycomb", "3.2.x", "July 15, 2011", false, 13, 13),
                    SingleAPILevel("Ice Cream Sandwich", "4.0 - 4.0.2", "October 18, 2011", false, 14, 14),
                    SingleAPILevel("Ice Cream Sandwich", "4.0.3 - 4.0.4", "December 16, 2011", false, 15, 15),
                    SingleAPILevel("Jelly Bean", "4.1.x", "July 9, 2012", false, 16, 16),
                    SingleAPILevel("Jelly Bean", "4.2.x", "November 13, 2012", false, 17, 17),
                    SingleAPILevel("Jelly Bean", "4.3.x", "July 24, 2013", false, 18, 18),
                    SingleAPILevel("kitKat", "4.4 - 4.4.4", "October 31, 2013", false, 19, 19),
                    SingleAPILevel("Lollipop", "5.0", "November 12, 2014", false, 21, 21),
                    SingleAPILevel("Lollipop", "5.1", "March 9, 2015", false, 22, 22),
                    SingleAPILevel("Marshmallow", "6.0", "October 5, 2015", false, 23, 23),
                    SingleAPILevel("Nougat", "7.0", "August 22, 2016", false, 24, 24),
                    SingleAPILevel("Nougat", "7.1", "October 4, 2016", false, 25, 25),
                    SingleAPILevel("Oreo", "8.0.0", "August 21, 2017", true, 26, 26),
                    SingleAPILevel("Oreo", "8.1.0", "December 5, 2017", true, 27, 27),
                    SingleAPILevel("Pie", "9", "August 6, 2018", true, 28, 28),
                    SingleAPILevel("Android10", "10", "September 3, 2019", true, 29, 29),
                    SingleAPILevel("Android11", "11", "September 8, 2020", true, 30, 30),
                    SingleAPILevel("Android12", "12", "October 4, 2021", true, 31, 31),
                )

            return items.sortedWith(compareByDescending({it.apiLevelStart}))
        }
    }
}