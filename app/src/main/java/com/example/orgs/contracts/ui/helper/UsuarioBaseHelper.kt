package com.example.orgs.contracts.ui.helper

import com.example.orgs.data.model.Usuario
import com.example.orgs.data.model.UsuarioWithProdutos
import kotlinx.coroutines.flow.StateFlow

interface UsuarioBaseHelper {
    val usuario: StateFlow<Usuario?>
    val usuariosWithProdutos: StateFlow<List<UsuarioWithProdutos>?>

    suspend fun verifyUsuarioLogged()

    suspend fun findUsuariosWithProdutos(): List<UsuarioWithProdutos>

    suspend fun logout()
}