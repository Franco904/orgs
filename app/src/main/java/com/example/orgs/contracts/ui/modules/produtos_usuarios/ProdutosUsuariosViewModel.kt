package com.example.orgs.contracts.ui.modules.produtos_usuarios

import com.example.orgs.data.model.UsuarioWithProdutos
import kotlinx.coroutines.flow.StateFlow

interface ProdutosUsuariosViewModel {
    val usuariosWithProdutos: StateFlow<List<UsuarioWithProdutos>?>
}