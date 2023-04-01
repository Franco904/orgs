package com.example.orgs.contracts.ui.modules.lista_produtos

import com.example.orgs.data.enums.OrderingPattern
import com.example.orgs.data.enums.ProdutoField
import com.example.orgs.data.model.Produto
import kotlinx.coroutines.flow.StateFlow

interface ListaProdutosViewModel {
    val hasSessionExpired: StateFlow<Boolean>
    val produtos: StateFlow<List<Produto>>

    fun findProdutosOrderedByField(
        field: ProdutoField,
        orderingPattern: OrderingPattern,
    )

    fun deleteProduto(produto: Produto)
}