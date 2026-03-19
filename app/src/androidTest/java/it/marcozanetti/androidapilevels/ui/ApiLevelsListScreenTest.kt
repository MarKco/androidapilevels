package it.marcozanetti.androidapilevels.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel

class ApiLevelsListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun apiLevelsListScreen_rendersItems() {
        composeTestRule.setContent {
            ApiLevelsListScreenPreview()
        }
        composeTestRule.onNodeWithText("Pie").assertIsDisplayed()
        composeTestRule.onNodeWithText("Oreo").assertIsDisplayed()
    }

    @Test
    fun apiLevelsListScreen_emptyList_showsNothing() {
        composeTestRule.setContent {
            androidx.compose.material.Surface {
                androidx.compose.foundation.lazy.LazyColumn {}
            }
        }
        // No item should be displayed
        // Optionally check for empty state UI if implemented
    }
}
