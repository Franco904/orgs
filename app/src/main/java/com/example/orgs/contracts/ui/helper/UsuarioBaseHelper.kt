package com.example.orgs.contracts.ui.helper

import com.example.orgs.data.model.Usuario
import com.example.orgs.data.model.UsuarioWithProdutos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UsuarioBaseHelper {
    val usuario: StateFlow<Usuario?>

    suspend fun verifyUsuarioLogged()

    fun findUsuariosWithProdutos(): Flow<List<UsuarioWithProdutos>>

    suspend fun logout()
}