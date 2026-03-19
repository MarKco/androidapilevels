package it.marcozanetti.androidapilevels.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test

class MainScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainScreen_rendersTopBarAndList() {
        composeTestRule.setContent {
            MainScreen()
        }
        composeTestRule.onNodeWithText("Android API Levels").assertIsDisplayed()
    }

    @Test
    fun mainScreen_searchIcon_opensSearchField() {
        composeTestRule.setContent {
            MainScreen()
        }
        composeTestRule.onNodeWithContentDescription("Cerca").performClick()
        composeTestRule.onNodeWithText("Cerca...").assertIsDisplayed()
    }

    @Test
    fun mainScreen_loadingIndicator_showsWhenLoading() {
        // TODO: Mock ViewModel to set isLoading = true
        // This test is a placeholder for when ViewModel is mockable
    }
}
