package com.example.orgs.data.database.repositories

import com.example.orgs.data.database.dao.ProdutosDao
import com.example.orgs.data.enums.OrderingPattern
import com.example.orgs.data.enums.ProdutoField
import com.example.orgs.data.model.Produto
import kotlinx.coroutines.flow.Flow

class ProdutosRepository(
    private val dao: ProdutosDao,
) {
    suspend fun create(produto: Produto) = dao.create(produto)

    suspend fun delete(produto: Produto) = dao.delete(produto)

    suspend fun findById(id: Long): Produto = dao.findById(id)

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