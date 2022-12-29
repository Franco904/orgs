package com.example.orgs.database.dao

import androidx.room.*
import com.example.orgs.model.Produto

@Dao
interface ProdutosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(vararg produto: Produto)

    @Delete
    fun delete(vararg produto: Produto)

    @Query("SELECT * FROM Produto")
    fun findAll(): List<Produto>

    @Query("SELECT * FROM Produto WHERE id = :id")
    fun findById(id: Long): Produto
}