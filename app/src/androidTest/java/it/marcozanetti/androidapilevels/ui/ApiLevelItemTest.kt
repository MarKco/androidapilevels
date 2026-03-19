package it.marcozanetti.androidapilevels.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertHasClickAction
import org.junit.Rule
import org.junit.Test
import it.marcozanetti.androidapilevels.apilevelslist.model.SingleAPILevel

class ApiLevelItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun apiLevelItem_displaysCorrectTexts() {
        val item = SingleAPILevel(
            codeName = "Pie",
            versionNumber = "9.0",
            releaseDate = "August 6, 2018",
            supported = true,
            apiLevelStart = 28f,
            apiLevelEnd = 28f,
            logoResourceId = 0
        )
        composeTestRule.setContent {
            ApiLevelItem(item = item, onClick = {})
        }
        composeTestRule.onNodeWithText("Pie").assertIsDisplayed()
        composeTestRule.onNodeWithText("Android 9.0 - API 28").assertIsDisplayed()
        composeTestRule.onNodeWithText("August 6, 2018").assertIsDisplayed()
        composeTestRule.onNodeWithText("Supported").assertIsDisplayed()
    }

    @Test
    fun apiLevelItem_clickable() {
        val item = SingleAPILevel(
            codeName = "Pie",
            versionNumber = "9.0",
            releaseDate = "August 6, 2018",
            supported = true,
            apiLevelStart = 28f,
            apiLevelEnd = 28f,
            logoResourceId = 0
        )
        var clicked = false
        composeTestRule.setContent {
            ApiLevelItem(item = item, onClick = { clicked = true })
        }
        composeTestRule.onNodeWithText("Pie").assertHasClickAction()
        composeTestRule.onNodeWithText("Pie").performClick()
        assert(clicked)
    }

    @Test
    fun apiLevelItem_noLogo_displaysVersionNumber() {
        val item = SingleAPILevel(
            codeName = "Pie",
            versionNumber = "9.0",
            releaseDate = "August 6, 2018",
            supported = true,
            apiLevelStart = 28f,
            apiLevelEnd = 28f,
            logoResourceId = 0
        )
        composeTestRule.setContent {
            ApiLevelItem(item = item, onClick = {})
        }
        composeTestRule.onNodeWithText("9.0").assertIsDisplayed()
    }
}

