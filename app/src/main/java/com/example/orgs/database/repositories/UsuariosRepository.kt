package com.example.orgs.database.repositories

import android.content.Context
import com.example.orgs.database.AppDatabase
import com.example.orgs.model.Usuario
import kotlinx.coroutines.flow.Flow

class UsuariosRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).usuariosDao()

    suspend fun create(usuario: Usuario) = dao.create(usuario)

    suspend fun findByUserAndPassword(usuario: String, senha: String): Usuario? {
        return dao.findByUserAndPassword(usuario, senha)
    }

    fun findByNameId(nameId: String): Flow<Usuario> {
        return dao.findByNameId(nameId)
    }
}