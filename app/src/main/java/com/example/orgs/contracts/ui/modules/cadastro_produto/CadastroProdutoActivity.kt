package com.example.orgs.contracts.ui.modules.cadastro_produto

import com.example.orgs.data.model.Produto

interface CadastroProdutoActivity {
    fun bindEditProdutoDataIfNeeded(produto: Produto)

    fun setUpOnSaveListener()

    fun setUpOnImageTappedListener()

    fun createProduto(usuarioId: Long?): Produto
}