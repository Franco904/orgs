package com.example.orgs.contracts.ui

import com.example.orgs.data.model.Usuario

interface CadastroUsuarioActivity {
    fun setUpSignUpButtonListener()

    fun signUp()

    fun createUsuario(): Usuario
}