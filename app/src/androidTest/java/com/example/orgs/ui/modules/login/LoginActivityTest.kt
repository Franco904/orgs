package com.example.orgs.ui.modules.login

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.*
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions as va
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.orgs.contracts.ui.modules.login.LoginViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.example.orgs.R
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginActivityTest {
    private lateinit var scenario: ActivityScenario<LoginActivityImpl>

    @BindValue
    @JvmField
    val viewModel: LoginViewModel = mockk<LoginViewModelImpl>(relaxed = true)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivityImpl> =
        ActivityScenarioRule(LoginActivityImpl::class.java)

    @Before
    fun setUp() {
        // Start activity
        scenario = launch(LoginActivityImpl::class.java)
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
        onView(withChild(withId(R.id.login_field_usuario))).check(matches(isDisplayed()))

        // Senha
        onView(withHint("Senha")).check(matches(isDisplayed()))
        onView(withChild(withId(R.id.login_field_senha))).check(matches(isDisplayed()))

        // Buttons
        onView(withText("Entrar")).check(matches(isDisplayed())).check(matches(isEnabled()))

        onView(withText("Cadastrar usuário")).check(matches(isDisplayed()))
            .check(matches(isEnabled()))
    }

    @Test
    fun mustPerformLogin() {
        onView(withId(R.id.login_field_usuario)).perform(
            typeText("francostavares2003@gmail.com"),
            va.pressBack(),
        )

        onView(withId(R.id.login_field_senha)).perform(
            typeText("123123as"),
            va.pressBack(),
        )

        onView(withId(R.id.login_btn_entrar)).perform(click())
    }
}