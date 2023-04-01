package com.example.orgs.ui.modules.produtos_usuarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orgs.contracts.data.database.repositories.UsuariosRepository
import com.example.orgs.contracts.ui.helper.UsuarioBaseHelper
import com.example.orgs.contracts.ui.modules.produtos_usuarios.ProdutosUsuariosViewModel
import com.example.orgs.data.model.UsuarioWithProdutos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProdutosUsuariosViewModelImpl @Inject constructor(
    private val usuariosRepository: UsuariosRepository,
) : ViewModel(), ProdutosUsuariosViewModel {
    private val _hasSessionExpired = MutableStateFlow(false)
    override val hasSessionExpired: StateFlow<Boolean> = _hasSessionExpired

    private val _usuariosWithProdutos = MutableStateFlow<List<UsuarioWithProdutos>?>(null)
    override val usuariosWithProdutos: StateFlow<List<UsuarioWithProdutos>?> = _usuariosWithProdutos

    init {
        viewModelScope.launch {
            setUpUsuarioLoggedListener()
        }

        viewModelScope.launch {
            setUpUsuarioWithProdutosData()
        }
    }

    private suspend fun setUpUsuarioLoggedListener() {
        usuariosRepository.watchLogged().collect { usuarioName ->
            if (usuarioName.isNullOrEmpty()) {
                setSessionHasExpired()
            }
        }
    }

    private fun setSessionHasExpired() {
        _hasSessionExpired.value = true
    }

    private suspend fun setUpUsuarioWithProdutosData() {
        _usuariosWithProdutos.value = usuariosRepository.findAllWithProdutos()
    }
}