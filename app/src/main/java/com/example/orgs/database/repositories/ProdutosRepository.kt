package com.example.orgs.database.repositories

import android.content.Context
import androidx.room.Query
import com.example.orgs.database.AppDatabase
import com.example.orgs.database.dao.ProdutosDao
import com.example.orgs.enums.OrderingPattern
import com.example.orgs.enums.ProdutoField
import com.example.orgs.model.Produto
import kotlinx.coroutines.flow.Flow

class ProdutosRepository(context: Context) {
    private val dao: ProdutosDao = AppDatabase.getInstance(context).produtosDao()

    suspend fun create(produto: Produto) = dao.create(produto)

    suspend fun delete(produto: Produto) = dao.delete(produto)

    suspend fun findById(id: Long): Produto = dao.findById(id)

    fun findAll(): Flow<List<Produto>> = dao.findAll()

    fun findAllByUsuarioId(usuarioId: Long): Flow<List<Produto>> {
        return dao.findAllByUsuarioId(usuarioId)
    }

    suspend fun findAllOrderedByField(
        field: ProdutoField,
        orderingPattern: OrderingPattern,
    ): List<Produto> {
        return if (orderingPattern == OrderingPattern.ASC) {
            when (field) {
                ProdutoField.TITULO -> dao.findAllOrderedByTituloAsc()
                ProdutoField.DESCRICAO -> dao.findAllOrderedByDescricaoAsc()
                ProdutoField.VALOR -> dao.findAllOrderedByValorAsc()
            }
        } else {
            when (field) {
                ProdutoField.TITULO -> dao.findAllOrderedByTituloDesc()
                ProdutoField.DESCRICAO -> dao.findAllOrderedByDescricaoDesc()
                ProdutoField.VALOR -> dao.findAllOrderedByValorDesc()
            }
        }
    }
}