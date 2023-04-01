package com.example.orgs.ui.modules.cadastro_produto

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orgs.contracts.data.database.repositories.ProdutosRepository
import com.example.orgs.contracts.data.database.repositories.UsuariosRepository
import com.example.orgs.contracts.ui.modules.cadastro_produto.CadastroProdutoViewModel
import com.example.orgs.data.model.Produto
import com.example.orgs.data.model.Usuario
import com.example.orgs.util.constants.ID_DEFAULT
import com.example.orgs.util.constants.PRODUTO_ID_EXTRA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CadastroProdutoViewModelImpl @Inject constructor(
    private val produtosRepository: ProdutosRepository,
    private val usuariosRepository: UsuariosRepository,
    private val state: SavedStateHandle,
) : ViewModel(), CadastroProdutoViewModel {
    private val _usuario = MutableStateFlow<Usuario?>(null)
    override val usuario: StateFlow<Usuario?> = _usuario

    private val _hasSessionExpired = MutableStateFlow(false)
    override val hasSessionExpired: StateFlow<Boolean> = _hasSessionExpired

    private val _produtoToEdit = MutableStateFlow<Produto?>(null)
    override val produtoToEdit: StateFlow<Produto?> = _produtoToEdit

    private var produtoToEditId: Long = ID_DEFAULT

    init {
        viewModelScope.launch {
            setUpUsuarioLoggedListener()
        }

        getIntentData()
    }

    private suspend fun setUpUsuarioLoggedListener() {
        usuariosRepository.watchLogged().collect { usuarioName ->
            usuarioName?.let {
                _usuario.value = usuariosRepository.findByNameId(it).firstOrNull()
            } ?: setSessionHasExpired()
        }
    }

    private fun setSessionHasExpired() {
        _hasSessionExpired.value = true
    }

    private fun getIntentData() {
        produtoToEditId = state.get<Long>(PRODUTO_ID_EXTRA) ?: ID_DEFAULT
    }

    override fun tryFindProdutoInDatabase() {
        viewModelScope.launch {
            _produtoToEdit.value = produtosRepository.findById(produtoToEditId)
        }
    }

    override fun createProdutoInDatabase(produto: Produto) {
        viewModelScope.launch {
            produtosRepository.create(produto)
        }
    }
}