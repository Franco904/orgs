package com.example.orgs.ui.modules.lista_produtos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orgs.contracts.data.database.repositories.ProdutosRepository
import com.example.orgs.contracts.ui.helper.UsuarioBaseHelper
import com.example.orgs.contracts.ui.modules.lista_produtos.ListaProdutosViewModel
import com.example.orgs.data.enums.OrderingPattern
import com.example.orgs.data.enums.ProdutoField
import com.example.orgs.data.model.Produto
import com.example.orgs.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaProdutosViewModelImpl @Inject constructor(
    private val produtosRepository: ProdutosRepository,
    private val usuarioHelper: UsuarioBaseHelper,
) : ViewModel(), ListaProdutosViewModel {
    override val usuario: StateFlow<Usuario?> by lazy { usuarioHelper.usuario }

    private val _produtos = MutableStateFlow<List<Produto>>(mutableListOf())
    override val produtos: StateFlow<List<Produto>> = _produtos

    init {
        viewModelScope.launch {
            usuarioHelper.verifyUsuarioLogged()
        }

        viewModelScope.launch {
            setUpUsuarioStateListener()
        }
    }

    private suspend fun setUpUsuarioStateListener() {
        usuario.filterNotNull().collect { usuario ->
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
            usuario.value?.let { usuario ->
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