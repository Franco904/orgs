package com.example.orgs.contracts.ui

interface PerfilUsuarioActivity {
    suspend fun bindUsuarioData()

    fun setUpLogoutButtonListener()
}