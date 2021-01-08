package it.marcozanetti.androidapilevels.apilevelslist.model

import org.junit.Assert.*
import org.junit.Test

class APILevelsContentTest {

    @Test
    fun getAPILevels_retrieve_checkNumber() {
        val apiLevels = APILevelsContent.getAPILevels()
        assertEquals(apiLevels.size, 18)
    }

    @Test
    fun getAPILevels_retrieve_checkType() {
        val apiLevels = APILevelsContent.getAPILevels()
        for(singleAPI in apiLevels) {
            assert(singleAPI is SingleAPILevel)
        }
    }
}