package com.example.orgs.data.database.repositories

import com.example.orgs.contracts.data.database.repositories.UsuariosRepository
import com.example.orgs.contracts.infra.preferences.UsuariosPreferences
import com.example.orgs.data.database.dao.UsuariosDao
import com.example.orgs.data.model.Usuario
import com.example.orgs.data.model.UsuarioWithProdutos
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsuariosRepositoryImpl @Inject constructor(
    private val dao: UsuariosDao,
    private val preferences: UsuariosPreferences,
): UsuariosRepository {
    override suspend fun create(usuario: Usuario) = dao.create(usuario)

    override suspend fun findByUserAndPassword(usuario: String, senha: String): Usuario? {
        return dao.findByUserAndPassword(usuario, senha)
    }

    override fun findByNameId(nameId: String): Flow<Usuario> {
        return dao.findByNameId(nameId)
    }

    override suspend fun findAllWithProdutos(): List<UsuarioWithProdutos> {
        return dao.findAllWithProdutos()
    }

    override fun watchLogged() = preferences.watchUsuarioName()

    override suspend fun writeUsuario(usuarioName: String) = preferences.writeUsuarioName(usuarioName)

    override suspend fun logout() = preferences.removeUsuarioName()
}