package com.example.orgs.database.repositories

import android.content.Context
import com.example.orgs.database.AppDatabase
import com.example.orgs.database.dao.ProdutosDao
import com.example.orgs.enums.OrderingPattern
import com.example.orgs.enums.ProdutoField
import com.example.orgs.model.Produto

class ProdutosRepository(context: Context) {
    private val dao: ProdutosDao = AppDatabase.getInstance(context).produtosDao()

    fun create(produto: Produto) = dao.create(produto)

    fun delete(produto: Produto) = dao.delete(produto)

    fun findById(id: Long): Produto = dao.findById(id)

    fun findAll(): List<Produto> = dao.findAll()

    fun findAllOrderedByField(
        field: ProdutoField,
        orderingPattern: OrderingPattern,
    ): List<Produto> {
        return if (orderingPattern == OrderingPattern.ASC) {
            when (field) {
                ProdutoField.TITULO -> dao.findAllOrderedByTituloAsc()
                ProdutoField.DESCRICAO -> dao.findAllOrderedByDescricaoAsc()
                ProdutoField.VALOR -> dao.findAllOrderedByValorAsc()
                else -> dao.findAll()
            }
        } else {
            when (field) {
                ProdutoField.TITULO -> dao.findAllOrderedByTituloDesc()
                ProdutoField.DESCRICAO -> dao.findAllOrderedByDescricaoDesc()
                ProdutoField.VALOR -> dao.findAllOrderedByValorDesc()
                else -> dao.findAll()
            }
        }
    }
}