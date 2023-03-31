package com.example.orgs.ui.modules.cadastro_usuario

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.*
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.orgs.R
import com.example.orgs.contracts.ui.modules.cadastro_usuario.CadastroUsuarioViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CadastroUsuarioActivityTest {
    private lateinit var scenario: ActivityScenario<CadastroUsuarioActivityImpl>

    @BindValue
    @JvmField
    val viewModel: CadastroUsuarioViewModel = mockk<CadastroUsuarioViewModelImpl>(relaxed = true)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityRule: ActivityScenarioRule<CadastroUsuarioActivityImpl> =
        ActivityScenarioRule(CadastroUsuarioActivityImpl::class.java)

    @Before
    fun setUp() {
        // Start activity
        scenario = launch(CadastroUsuarioActivityImpl::class.java)
    }

    @After
    fun tearDown() {
        // Close activity
        scenario.close()
    }

    @Test
    fun mustShowFormWidgetsOnFirstLoad() {
        // Usuario (Email)
        onView(withHint("Usuário (Email)")).check(matches(isDisplayed()))
        onView(withChild(withId(R.id.cadastro_usuario_field_usuario))).check(matches(isDisplayed()))

        // Nome
        onView(withHint("Nome")).check(matches(isDisplayed()))
        onView(withChild(withId(R.id.cadastro_usuario_field_nome))).check(matches(isDisplayed()))

        // Senha
        onView(withHint("Senha")).check(matches(isDisplayed()))
        onView(withChild(withId(R.id.cadastro_usuario_field_senha))).check(matches(isDisplayed()))

        // Button
        onView(withText("Cadastrar"))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
    }

    @Test
    fun mustPerformUsuarioRegister() {
        onView(withId(R.id.cadastro_usuario_field_usuario)).perform(
            ViewActions.typeText("francostavares2003@gmail.com"),
            ViewActions.closeSoftKeyboard(),
        )

        onView(withId(R.id.cadastro_usuario_field_nome)).perform(
            ViewActions.typeText("Franco Tavares"),
            ViewActions.closeSoftKeyboard(),
        )

        onView(withId(R.id.cadastro_usuario_field_senha)).perform(
            ViewActions.typeText("123123as"),
            ViewActions.closeSoftKeyboard(),
        )

        onView(withId(R.id.cadastro_usuario_btn_cadastrar)).perform(ViewActions.click())
    }
}