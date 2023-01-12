package com.example.orgs.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.orgs.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuariosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(vararg usuario: Usuario)

    @Query("SELECT * FROM Usuario WHERE usuario = :usuario AND senha = :senha")
    suspend fun findByUserAndPassword(usuario: String, senha: String): Usuario?

    @Query("SELECT * FROM Usuario WHERE usuario = :nameId")
    fun findByNameId(nameId: String): Flow<Usuario>
}