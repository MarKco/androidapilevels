package it.marcozanetti.androidapilevels.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test
import it.marcozanetti.androidapilevels.ui.theme.AndroidAPILevelsTheme

class AndroidAPILevelsThemeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun androidAPILevelsTheme_appliesLightTheme() {
        composeTestRule.setContent {
            AndroidAPILevelsTheme(darkTheme = false) {
                androidx.compose.material.Text("Light Theme")
            }
        }
        composeTestRule.onNodeWithText("Light Theme").assertIsDisplayed()
    }

    @Test
    fun androidAPILevelsTheme_appliesDarkTheme() {
        composeTestRule.setContent {
            AndroidAPILevelsTheme(darkTheme = true) {
                androidx.compose.material.Text("Dark Theme")
            }
        }
        composeTestRule.onNodeWithText("Dark Theme").assertIsDisplayed()
    }
}

