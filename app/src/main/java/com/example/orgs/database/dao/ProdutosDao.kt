package com.example.orgs.database.dao

import androidx.room.*
import com.example.orgs.model.Produto

@Dao
interface ProdutosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(vararg produto: Produto)

    @Delete
    fun delete(vararg produto: Produto)

    @Query("SELECT * FROM Produto WHERE id = :id")
    fun findById(id: Long): Produto

    @Query("SELECT * FROM Produto")
    fun findAll(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY titulo")
    fun findAllOrderedByTituloAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao")
    fun findAllOrderedByDescricaoAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor")
    fun findAllOrderedByValorAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY titulo DESC")
    fun findAllOrderedByTituloDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao DESC")
    fun findAllOrderedByDescricaoDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor DESC")
    fun findAllOrderedByValorDesc(): List<Produto>
}