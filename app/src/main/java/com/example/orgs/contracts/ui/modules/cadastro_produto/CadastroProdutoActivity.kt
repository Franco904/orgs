package com.example.orgs.contracts.ui.modules.cadastro_produto

import com.example.orgs.data.model.Produto

interface CadastroProdutoActivity {
    fun getIntentData()

    fun tryFindProdutoInDatabase()

    fun bindEditProdutoDataIfNeeded()

    fun setUpOnSaveListener()

    fun setUpOnImageTappedListener()

    fun createProduto(usuarioId: Long?): Produto
}