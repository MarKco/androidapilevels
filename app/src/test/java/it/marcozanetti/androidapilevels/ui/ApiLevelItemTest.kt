package it.marcozanetti.androidapilevels.ui

import org.junit.Assert.*
import org.junit.Test
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel

class ApiLevelItemTest {
    @Test
    fun testToString_withEndLevel() {
        val item = SingleAPILevel(
            codeName = "Pie",
            versionNumber = "9.0",
            releaseDate = "August 6, 2018",
            supported = true,
            apiLevelStart = 28f,
            apiLevelEnd = 29f,
            logoResourceId = 0,
            apiName = "TIRAMISU"
        )
        val str = item.toString()
        assertTrue(str.contains("Pie"))
        assertTrue(str.contains("28"))
        assertTrue(str.contains("29"))
    }

    @Test
    fun testToString_noEndLevel() {
        val item = SingleAPILevel(
            codeName = "Pie",
            versionNumber = "9.0",
            releaseDate = "August 6, 2018",
            supported = true,
            apiLevelStart = 28f,
            apiLevelEnd = 28f,
            logoResourceId = 0,
            apiName = "TIRAMISU"
        )
        val str = item.toString()
        assertTrue(str.contains("Pie"))
        assertTrue(str.contains("28"))
        assertFalse(str.contains("29"))
    }

    @Test
    fun testGetApiText() {
        val item = SingleAPILevel(
            codeName = "Pie",
            versionNumber = "9.0",
            releaseDate = "August 6, 2018",
            supported = true,
            apiLevelStart = 28f,
            apiLevelEnd = 29f,
            logoResourceId = 0,
            apiName = "TIRAMISU"
        )
        assertEquals("API 28–29", item.getApiText())
    }

    @Test
    fun testGetApiText_single() {
        val item = SingleAPILevel(
            codeName = "Pie",
            versionNumber = "9.0",
            releaseDate = "August 6, 2018",
            supported = true,
            apiLevelStart = 28f,
            apiLevelEnd = 28f,
            logoResourceId = 0,
            apiName = "TIRAMISU"
        )
        assertEquals("API 28", item.getApiText())
    }

    @Test
    fun testEdgeCases_emptyStrings() {
        val item = SingleAPILevel(
            codeName = "",
            versionNumber = "",
            releaseDate = "",
            supported = false,
            apiLevelStart = -1f,
            apiLevelEnd = -1f,
            logoResourceId = 0,
            apiName = null
        )
        assertEquals("API -1", item.getApiText())
        val str = item.toString()
        assertTrue(str.contains("version "))
    }
}
