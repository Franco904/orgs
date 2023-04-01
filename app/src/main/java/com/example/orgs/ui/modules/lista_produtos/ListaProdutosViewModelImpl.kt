package com.example.orgs.ui.modules.lista_produtos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orgs.contracts.data.database.repositories.ProdutosRepository
import com.example.orgs.contracts.data.database.repositories.UsuariosRepository
import com.example.orgs.contracts.ui.helper.UsuarioBaseHelper
import com.example.orgs.contracts.ui.modules.lista_produtos.ListaProdutosViewModel
import com.example.orgs.data.enums.OrderingPattern
import com.example.orgs.data.enums.ProdutoField
import com.example.orgs.data.model.Produto
import com.example.orgs.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaProdutosViewModelImpl @Inject constructor(
    private val produtosRepository: ProdutosRepository,
    private val usuariosRepository: UsuariosRepository,
) : ViewModel(), ListaProdutosViewModel {
    private val _usuario = MutableStateFlow<Usuario?>(null)

    private val _hasSessionExpired = MutableStateFlow(false)
    override val hasSessionExpired: StateFlow<Boolean> = _hasSessionExpired

    private val _produtos = MutableStateFlow<List<Produto>>(mutableListOf())
    override val produtos: StateFlow<List<Produto>> = _produtos

    init {
        viewModelScope.launch {
            setUpUsuarioLoggedListener()
        }

        viewModelScope.launch {
            setUpUsuarioStateListener()
        }
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

    private suspend fun setUpUsuarioStateListener() {
        _usuario.filterNotNull().collect { usuario ->
            getProdutosAndNotifyListener(usuarioId = usuario.id!!)
        }
    }

    private fun getProdutosAndNotifyListener(usuarioId: Long) {
        viewModelScope.launch {
            produtosRepository.findAllByUsuarioId(usuarioId).collect { produtos ->
                _produtos.value = produtos
            }
        }
    }

    override fun findProdutosOrderedByField(
        field: ProdutoField,
        orderingPattern: OrderingPattern,
    ) {
        viewModelScope.launch {
            _usuario.value?.let { usuario ->
                _produtos.value = produtosRepository.findAllOrderedByField(
                    field,
                    orderingPattern,
                    usuarioId = usuario.id!!,
                )
            }
        }
    }

    override fun deleteProduto(produto: Produto) {
        viewModelScope.launch {
            produtosRepository.delete(produto)
        }
    }
}