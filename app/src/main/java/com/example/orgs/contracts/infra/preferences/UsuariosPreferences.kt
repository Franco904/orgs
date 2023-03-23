package com.example.orgs.contracts.infra.preferences

import kotlinx.coroutines.flow.Flow

interface UsuariosPreferences {
    suspend fun writeUsuarioName(usuarioName: String)

    fun watchUsuarioName(): Flow<String?>

    suspend fun removeUsuarioName()
}