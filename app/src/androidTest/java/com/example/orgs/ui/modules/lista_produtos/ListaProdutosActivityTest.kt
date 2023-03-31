package com.example.orgs.ui.modules.lista_produtos

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.*
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.orgs.R
import com.example.orgs.contracts.ui.modules.lista_produtos.ListaProdutosViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ListaProdutosActivityTest {
    lateinit var scenario: ActivityScenario<ListaProdutosActivityImpl>

    @BindValue
    @JvmField
    val viewModel: ListaProdutosViewModel = mockk<ListaProdutosViewModelImpl>(relaxed = true) // Precisa especificar tipo da interface

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityRule: ActivityScenarioRule<ListaProdutosActivityImpl> = ActivityScenarioRule(ListaProdutosActivityImpl::class.java)

    @Before
    fun setUp() {
        // Start activity
        scenario = launch(ListaProdutosActivityImpl::class.java)
    }

    @After
    fun tearDown() {
        // Close activity
        scenario.close()
    }

    @Test
    fun mustDisplayCorrectActivityNameInAppbar() {
        onView(withText("Lista de produtos")).check(matches(isDisplayed()))
    }

    @Test
    fun mustDisplayCadastroProdutoFAB() {
        onView(withId(R.id.lista_produtos_fab)).check(matches(isDisplayed()))
        onView(withText("Novo produto")).check(matches(isDisplayed()))
    }
}