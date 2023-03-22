package com.example.orgs.contracts.ui.modules.lista_produtos

import com.example.orgs.data.model.Produto

interface ListaProdutosActivity {
    fun setUpOrderingDropdowns()

    fun updateProdutosList(produtos: List<Produto>)

    fun setUpRecyclerView()

    fun setUpFloatingActionButtonListener()

    fun setUpProdutoCardListeners()
}