package it.marcozanetti.androidapilevels.apilevelslist.model

import org.junit.Test

class DefaultDataProviderTest : junit.framework.TestCase() {

    @Test
    fun testDataProvider() {
        testNotEmptyDefaultList()
        testRightSizeForDefaultList()
    }

    @Test
    fun testNotEmptyDefaultList() {
        assertTrue(DefaultDataProvider.data.isNotEmpty())
    }

    @Test
    fun testRightSizeForDefaultList() {
        assertEquals(
            37,
            DefaultDataProvider.data.size
        )
    }

}