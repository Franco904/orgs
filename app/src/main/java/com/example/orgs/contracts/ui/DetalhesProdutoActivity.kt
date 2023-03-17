package com.example.orgs.contracts.ui

import com.example.orgs.data.model.Produto

interface DetalhesProdutoActivity {
    fun getIntentData()

    fun tryFindProdutoInDatabase()

    fun bindProdutoDataIfExist()

    fun setUpEditButtonListener()

    fun setUpDeleteButtonListener(produtoToDelete: Produto)
}