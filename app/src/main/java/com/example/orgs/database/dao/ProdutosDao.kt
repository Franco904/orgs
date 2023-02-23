package com.example.orgs.database.dao

import androidx.room.*
import com.example.orgs.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(vararg produto: Produto)

    @Delete
    suspend fun delete(vararg produto: Produto)

    @Query("SELECT * FROM Produto WHERE id = :id")
    suspend fun findById(id: Long): Produto

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId")
    fun findAllByUsuarioId(usuarioId: Long): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY titulo")
    suspend fun findAllOrderedByTituloAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao")
    suspend fun findAllOrderedByDescricaoAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor")
    suspend fun findAllOrderedByValorAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY titulo DESC")
    suspend fun findAllOrderedByTituloDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao DESC")
    suspend fun findAllOrderedByDescricaoDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor DESC")
    suspend fun findAllOrderedByValorDesc(): List<Produto>
}