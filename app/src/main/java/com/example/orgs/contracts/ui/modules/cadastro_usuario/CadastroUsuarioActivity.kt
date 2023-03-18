package com.example.orgs.contracts.ui.modules.cadastro_usuario

import com.example.orgs.data.model.Usuario

interface CadastroUsuarioActivity {
    fun setUpSignUpButtonListener()

    fun signUp()

    fun createUsuario(): Usuario
}