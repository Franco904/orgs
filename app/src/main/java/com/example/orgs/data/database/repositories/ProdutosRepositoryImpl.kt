package com.example.orgs.data.database.repositories

import com.example.orgs.contracts.data.database.repositories.ProdutosRepository
import com.example.orgs.data.database.dao.ProdutosDao
import com.example.orgs.data.enums.OrderingPattern
import com.example.orgs.data.enums.ProdutoField
import com.example.orgs.data.model.Produto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProdutosRepositoryImpl @Inject constructor(
    private val dao: ProdutosDao,
): ProdutosRepository {
    override suspend fun create(produto: Produto) = dao.create(produto)

    override suspend fun delete(produto: Produto) = dao.delete(produto)

    override suspend fun findById(id: Long): Produto = dao.findById(id)

    override fun findAllByUsuarioId(usuarioId: Long): Flow<List<Produto>> {
        return dao.findAllByUsuarioId(usuarioId)
    }

    override suspend fun findAllOrderedByField(
        field: ProdutoField,
        orderingPattern: OrderingPattern,
        usuarioId: Long,
    ): List<Produto> {
        return if (orderingPattern == OrderingPattern.ASC) {
            when (field) {
                ProdutoField.TITULO -> dao.findAllOrderedByTituloAsc(usuarioId)
                ProdutoField.DESCRICAO -> dao.findAllOrderedByDescricaoAsc(usuarioId)
                ProdutoField.VALOR -> dao.findAllOrderedByValorAsc(usuarioId)
            }
        } else {
            when (field) {
                ProdutoField.TITULO -> dao.findAllOrderedByTituloDesc(usuarioId)
                ProdutoField.DESCRICAO -> dao.findAllOrderedByDescricaoDesc(usuarioId)
                ProdutoField.VALOR -> dao.findAllOrderedByValorDesc(usuarioId)
            }
        }
    }
}