package com.example.orgs.contracts.ui.modules.lista_produtos

import com.example.orgs.data.enums.OrderingPattern
import com.example.orgs.data.enums.ProdutoField
import com.example.orgs.data.model.Produto
import com.example.orgs.data.model.Usuario
import kotlinx.coroutines.flow.StateFlow

interface ListaProdutosViewModel {
    val usuario: StateFlow<Usuario?>
    val produtos: StateFlow<List<Produto>>

    suspend fun findProdutosOrderedByField(
        field: ProdutoField,
        orderingPattern: OrderingPattern,
    )

    suspend fun deleteProduto(produto: Produto)
}