package com.example.orgs.ui.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.junit.Test

class ListaProdutosActivityTest {
    @Test
    fun mustDisplayCorrectActivityNameInAppbar() {
        // Arrange activity view
        val scenario = ActivityScenario.launch(ListaProdutosActivityImpl::class.java)

        // Assert widget text
        val viewInteraction = Espresso.onView(ViewMatchers.withText("Lista de produtos"))
        viewInteraction.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Close activity
        scenario.close()
    }
}