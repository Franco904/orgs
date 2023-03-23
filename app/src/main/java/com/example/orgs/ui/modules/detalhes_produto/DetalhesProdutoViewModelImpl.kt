package com.example.orgs.ui.modules.detalhes_produto

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orgs.contracts.data.database.repositories.ProdutosRepository
import com.example.orgs.contracts.ui.modules.detalhes_produto.DetalhesProdutoViewModel
import com.example.orgs.data.model.Produto
import com.example.orgs.util.constants.ID_DEFAULT
import com.example.orgs.util.constants.PRODUTO_ID_EXTRA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalhesProdutoViewModelImpl @Inject constructor(
    private val produtosRepository: ProdutosRepository,
    private val state: SavedStateHandle,
) : ViewModel(), DetalhesProdutoViewModel {
    private val _produto = MutableStateFlow<Produto?>(null)
    override val produto: StateFlow<Produto?> = _produto

    private var produtoId: Long = ID_DEFAULT

    init {
        getIntentData()
    }

    private fun getIntentData() {
        produtoId = state.get<Long>(PRODUTO_ID_EXTRA) ?: ID_DEFAULT
    }

    override fun tryFindProdutoInDatabase() {
        viewModelScope.launch {
            _produto.value = produtosRepository.findById(produtoId)
        }
    }

    override fun deleteProduto() {
        viewModelScope.launch {
            produtosRepository.delete(_produto.value!!)
        }
    }
}