package com.example.orgs.contracts.data.database.repositories

import com.example.orgs.data.model.Usuario
import com.example.orgs.data.model.UsuarioWithProdutos
import kotlinx.coroutines.flow.Flow

interface UsuariosRepository {
    suspend fun create(usuario: Usuario)

    suspend fun findByUserAndPassword(usuario: String, senha: String): Usuario?

    fun findByNameId(nameId: String): Flow<Usuario>

    suspend fun findAllWithProdutos(): List<UsuarioWithProdutos>

    fun watchLogged(): Flow<String?>

    suspend fun writeUsuario(usuarioName: String)

    suspend fun logout()
}