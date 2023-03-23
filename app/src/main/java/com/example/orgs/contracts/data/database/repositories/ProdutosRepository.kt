package com.example.orgs.contracts.data.database.repositories

import com.example.orgs.data.enums.OrderingPattern
import com.example.orgs.data.enums.ProdutoField
import com.example.orgs.data.model.Produto
import kotlinx.coroutines.flow.Flow

interface ProdutosRepository {
    suspend fun create(produto: Produto)

    suspend fun delete(produto: Produto)

    suspend fun findById(id: Long): Produto

    fun findAllByUsuarioId(usuarioId: Long): Flow<List<Produto>>

    suspend fun findAllOrderedByField(
        field: ProdutoField,
        orderingPattern: OrderingPattern,
        usuarioId: Long,
    ): List<Produto>
}