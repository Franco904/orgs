package com.example.orgs.ui.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListaProdutosActivityTest {
    @Test
    fun mustDisplayCorrectActivityNameInAppbar() {
        // Arrange activity view
        val scenario = ActivityScenario.launch(ListaProdutosActivity::class.java)

        // Assert widget text
        val viewInteraction = Espresso.onView(ViewMatchers.withText("Lista de produtos"))
        viewInteraction.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Close activity
        scenario.close()
    }
}