package com.example.orgs.ui.modules.cadastro_produto

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.*
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.orgs.R
import com.example.orgs.contracts.ui.modules.cadastro_produto.CadastroProdutoViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CadastroProdutoActivityTest {
    lateinit var scenario: ActivityScenario<CadastroProdutoActivityImpl>

    @BindValue
    @JvmField
    val viewModel: CadastroProdutoViewModel = mockk<CadastroProdutoViewModelImpl>(relaxed = true)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityRule: ActivityScenarioRule<CadastroProdutoActivityImpl> =
        ActivityScenarioRule(CadastroProdutoActivityImpl::class.java)

    @Before
    fun setUp() {
        // Start activity
        scenario = launch(CadastroProdutoActivityImpl::class.java)
    }

    @After
    fun tearDown() {
        // Close activity
        scenario.close()
    }

    @Test
    fun mustShowFormWidgetsOnFirstLoad() {
        // Título
        onView(withHint("Título")).check(matches(isDisplayed()))
        onView(withChild(withId(R.id.cadastro_produto_field_titulo))).check(matches(isDisplayed()))

        // Descrição
        onView(withHint("Descrição")).check(matches(isDisplayed()))
        onView(withChild(withId(R.id.cadastro_produto_field_descricao))).check(matches(isDisplayed()))

        // Valor
        onView(withHint("Valor")).check(matches(isDisplayed()))
        onView(withChild(withId(R.id.cadastro_produto_field_valor))).check(matches(isDisplayed()))

        // Button
        onView(withText("Salvar"))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
    }
}