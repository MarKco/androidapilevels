package it.marcozanetti.androidapilevels.apilevelslist.model

import org.junit.Assert.*
import org.junit.Test

class APILevelsRepositoryContentTest {

    @Test
    fun getAPILevels_retrieve_checkNumber() {
        val apiLevels = APILevelsRepositoryImpl.getAPILevels()
        assertEquals(apiLevels.size, 18)
    }

    @Test
    fun getAPILevels_retrieve_checkType() {
        val apiLevels = APILevelsRepositoryImpl.getAPILevels()
        for(singleAPI in apiLevels) {
            assert(singleAPI is SingleAPILevel)
        }
    }
}