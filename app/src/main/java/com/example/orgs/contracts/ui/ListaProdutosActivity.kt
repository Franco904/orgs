package com.example.orgs.contracts.ui

import com.example.orgs.data.model.Produto

interface ListaProdutosActivity {
    fun setUpOrderingDropdowns()

    fun updateProdutosList(produtos: List<Produto>)

    fun setUpRecyclerView()

    fun setUpFloatingActionButtonListener()

    fun setUpUsuarioStateListener()

    fun getProdutosAndNotifyListeners(usuarioId: Long)

    fun setUpProdutoCardListeners()

    fun deleteProduto(produto: Produto)
}