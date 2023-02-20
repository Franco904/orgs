package com.example.orgs.database.repositories

import com.example.orgs.database.dao.UsuariosDao
import com.example.orgs.model.Usuario
import com.example.orgs.model.UsuarioWithProdutos
import kotlinx.coroutines.flow.Flow

class UsuariosRepository(
    private val dao: UsuariosDao,
) {
    suspend fun create(usuario: Usuario) = dao.create(usuario)

    suspend fun findByUserAndPassword(usuario: String, senha: String): Usuario? {
        return dao.findByUserAndPassword(usuario, senha)
    }

    fun findByNameId(nameId: String): Flow<Usuario> {
        return dao.findByNameId(nameId)
    }

    fun findWithProdutos(): Flow<List<UsuarioWithProdutos>> {
        return dao.findWithProdutos()
    }
}