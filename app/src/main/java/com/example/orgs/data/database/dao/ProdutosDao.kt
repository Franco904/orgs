package com.example.orgs.data.database.dao

import androidx.room.*
import com.example.orgs.data.model.Produto
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

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY titulo")
    suspend fun findAllOrderedByTituloAsc(usuarioId: Long): List<Produto>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY descricao")
    suspend fun findAllOrderedByDescricaoAsc(usuarioId: Long): List<Produto>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY valor")
    suspend fun findAllOrderedByValorAsc(usuarioId: Long): List<Produto>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY titulo DESC")
    suspend fun findAllOrderedByTituloDesc(usuarioId: Long): List<Produto>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY descricao DESC")
    suspend fun findAllOrderedByDescricaoDesc(usuarioId: Long): List<Produto>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY valor DESC")
    suspend fun findAllOrderedByValorDesc(usuarioId: Long): List<Produto>
}