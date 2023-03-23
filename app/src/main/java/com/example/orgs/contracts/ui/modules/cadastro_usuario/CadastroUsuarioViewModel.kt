package com.example.orgs.contracts.ui.modules.cadastro_usuario

import com.example.orgs.data.model.Usuario

interface CadastroUsuarioViewModel {
    fun createUsuarioInDatabase(usuario: Usuario)
}