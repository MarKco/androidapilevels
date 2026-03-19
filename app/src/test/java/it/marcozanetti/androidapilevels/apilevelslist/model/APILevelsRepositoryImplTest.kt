package it.marcozanetti.androidapilevels.apilevelslist.model

import org.junit.Assert.*
import org.junit.Test
import kotlinx.coroutines.runBlocking

class APILevelsRepositoryImplTest {
    @Test
    fun testMergeWithDefaultData_enrichesFields() {
        val repo = APILevelsRepositoryImpl()
        val retrieved = listOf(
            SingleAPILevel("Pie", "9.0", "", true, 28f, 28f, 0, null),
            SingleAPILevel("Fake", "99.0", "", false, 99f, 99f, 0, null)
        )
        val defaults = listOf(
            SingleAPILevel("Pie", "9.0", "August 6, 2018", true, 28f, 28f, 123, "TIRAMISU")
        )
        val merged = repo.run {
            val method = this.javaClass.getDeclaredMethod("mergeWithDefaultData", List::class.java, List::class.java)
            method.isAccessible = true
            method.invoke(this, retrieved, defaults) as List<SingleAPILevel>
        }
        val pie = merged.first { it.versionNumber == "9.0" }
        assertEquals("August 6, 2018", pie.releaseDate)
        assertEquals(123, pie.logoResourceId)
        assertEquals("TIRAMISU", pie.apiName)
        val fake = merged.first { it.versionNumber == "99.0" }
        assertEquals("", fake.releaseDate)
        assertEquals(0, fake.logoResourceId)
        assertNull(fake.apiName)
    }

    @Test
    fun testGetAPILevelsCompose_networkError() {
        val repo = APILevelsRepositoryImpl()
        // Simulate network error by using a mock or catching exception
        try {
            runBlocking {
                repo.getAPILevelsCompose()
            }
            fail("Should throw exception on network error")
        } catch (e: Exception) {
            assertTrue(e.message?.contains("HTTP error") ?: false)
        }
    }
}

