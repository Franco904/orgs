package com.example.orgs.ui.modules.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orgs.contracts.data.database.repositories.UsuariosRepository
import com.example.orgs.contracts.ui.helper.UsuarioBaseHelper
import com.example.orgs.contracts.ui.modules.perfil.PerfilUsuarioViewModel
import com.example.orgs.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilUsuarioViewModelImpl @Inject constructor(
    private val usuariosRepository: UsuariosRepository,
) : ViewModel(), PerfilUsuarioViewModel {
    private val _hasSessionExpired = MutableStateFlow(false)
    override val hasSessionExpired: StateFlow<Boolean> = _hasSessionExpired

    private val _usuario = MutableStateFlow<Usuario?>(null)
    override val usuario: StateFlow<Usuario?> = _usuario

    init {
        viewModelScope.launch {
            setUpUsuarioLoggedListener()
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

    override fun logoutUsuario() {
        viewModelScope.launch {
            usuariosRepository.logout()
        }
    }
}