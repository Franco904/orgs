package com.example.orgs.contracts.ui.helper

import com.example.orgs.data.model.UsuarioWithProdutos
import kotlinx.coroutines.flow.Flow

interface UsuarioBaseHelper {
    suspend fun verifyUsuarioLogged()

    fun findUsuariosWithProdutos(): Flow<List<UsuarioWithProdutos>>

    suspend fun logout()
}