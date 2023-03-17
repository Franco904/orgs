package com.example.orgs.contracts.ui

interface LoginActivity {
    fun setUpSignInButtonListener()

    fun setUpSignUpButtonListener()

    fun login(usuarioName: String, senha: String)
}