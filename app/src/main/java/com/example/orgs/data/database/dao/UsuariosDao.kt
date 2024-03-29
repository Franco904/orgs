package com.example.orgs.data.database.dao

import androidx.room.*
import com.example.orgs.data.model.Usuario
import com.example.orgs.data.model.UsuarioWithProdutos
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuariosDao {
    @Insert
    suspend fun create(vararg usuario: Usuario)

    @Query("SELECT * FROM Usuario WHERE usuario = :usuario AND senha = :senha")
    suspend fun findByUserAndPassword(usuario: String, senha: String): Usuario?

    @Query("SELECT * FROM Usuario WHERE usuario = :nameId")
    fun findByNameId(nameId: String): Flow<Usuario>

    @Transaction
    @Query("SELECT * FROM Usuario")
    suspend fun findAllWithProdutos(): List<UsuarioWithProdutos>
}