package com.example.orgs.contracts.ui.modules.cadastro_produto

import com.example.orgs.data.model.Produto
import com.example.orgs.data.model.Usuario
import kotlinx.coroutines.flow.StateFlow

interface CadastroProdutoViewModel {
    val usuario: StateFlow<Usuario?>
    val produtoToEdit: StateFlow<Produto?>

    suspend fun tryFindProdutoInDatabase()

    suspend fun createProdutoInDatabase(produto: Produto)
}