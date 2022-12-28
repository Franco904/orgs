package com.example.orgs.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.orgs.model.Produto

@Dao
interface ProdutosDao {
    @Insert
    fun create(vararg produto: Produto)

    @Query("SELECT * FROM Produto")
    fun findAll(): List<Produto>
}