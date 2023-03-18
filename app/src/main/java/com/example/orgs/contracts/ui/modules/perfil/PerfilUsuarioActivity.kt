package com.example.orgs.contracts.ui.modules.perfil

interface PerfilUsuarioActivity {
    suspend fun bindUsuarioData()

    fun setUpLogoutButtonListener()
}