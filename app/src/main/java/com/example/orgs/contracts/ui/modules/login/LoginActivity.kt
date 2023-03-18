package com.example.orgs.contracts.ui.modules.login

interface LoginActivity {
    fun setUpSignInButtonListener()

    fun setUpSignUpButtonListener()

    fun login(usuarioName: String, senha: String)
}