package com.example.orgs.contracts.ui.modules.perfil

import com.example.orgs.data.model.Usuario
import kotlinx.coroutines.flow.StateFlow

interface PerfilUsuarioViewModel {
    val usuario: StateFlow<Usuario?>

    fun logoutUsuario()
}