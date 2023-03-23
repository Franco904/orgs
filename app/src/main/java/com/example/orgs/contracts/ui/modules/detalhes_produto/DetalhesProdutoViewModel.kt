package com.example.orgs.contracts.ui.modules.detalhes_produto

import com.example.orgs.data.model.Produto
import kotlinx.coroutines.flow.StateFlow

interface DetalhesProdutoViewModel {
    val produto: StateFlow<Produto?>

    fun tryFindProdutoInDatabase()

    fun deleteProduto()
}