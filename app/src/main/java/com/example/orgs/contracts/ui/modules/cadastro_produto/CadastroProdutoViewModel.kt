package com.example.orgs.contracts.ui.modules.cadastro_produto

import com.example.orgs.data.model.Produto
import com.example.orgs.data.model.Usuario
import kotlinx.coroutines.flow.StateFlow

interface CadastroProdutoViewModel {
    val usuario: StateFlow<Usuario?>
    val hasSessionExpired: StateFlow<Boolean>
    val produtoToEdit: StateFlow<Produto?>

    fun tryFindProdutoInDatabase()

    fun createProdutoInDatabase(produto: Produto)
}