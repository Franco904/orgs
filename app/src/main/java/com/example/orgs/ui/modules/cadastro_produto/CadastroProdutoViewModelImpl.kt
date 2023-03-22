package com.example.orgs.ui.modules.cadastro_produto

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orgs.contracts.data.database.repositories.ProdutosRepository
import com.example.orgs.contracts.ui.helper.UsuarioBaseHelper
import com.example.orgs.contracts.ui.modules.cadastro_produto.CadastroProdutoViewModel
import com.example.orgs.data.model.Produto
import com.example.orgs.data.model.Usuario
import com.example.orgs.util.constants.ID_DEFAULT
import com.example.orgs.util.constants.PRODUTO_ID_EXTRA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CadastroProdutoViewModelImpl @Inject constructor(
    private val produtosRepository: ProdutosRepository,
    private val usuarioHelper: UsuarioBaseHelper,
    private val state: SavedStateHandle,
) : ViewModel(), CadastroProdutoViewModel {
    override val usuario: StateFlow<Usuario?> by lazy { usuarioHelper.usuario }

    private val _produtoToEdit = MutableStateFlow<Produto?>(null)
    override val produtoToEdit: StateFlow<Produto?> = _produtoToEdit

    private var produtoToEditId: Long = ID_DEFAULT

    init {
        viewModelScope.launch(Dispatchers.IO) {
            usuarioHelper.verifyUsuarioLogged()
        }

        getIntentData()
    }

    private fun getIntentData() {
        produtoToEditId = state.get<Long>(PRODUTO_ID_EXTRA) ?: ID_DEFAULT
    }

    override suspend fun tryFindProdutoInDatabase() {
        _produtoToEdit.value = produtosRepository.findById(produtoToEditId)
    }

    override suspend fun createProdutoInDatabase(produto: Produto) {
        produtosRepository.create(produto)
    }
}