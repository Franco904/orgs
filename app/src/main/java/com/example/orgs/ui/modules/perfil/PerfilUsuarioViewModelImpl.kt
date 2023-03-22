package com.example.orgs.ui.modules.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orgs.contracts.ui.helper.UsuarioBaseHelper
import com.example.orgs.contracts.ui.modules.perfil.PerfilUsuarioViewModel
import com.example.orgs.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilUsuarioViewModelImpl @Inject constructor(
    private val usuarioHelper: UsuarioBaseHelper,
) : ViewModel(), PerfilUsuarioViewModel {
    override val usuario: StateFlow<Usuario?> by lazy { usuarioHelper.usuario }

    init {
        viewModelScope.launch {
            usuarioHelper.verifyUsuarioLogged()
        }
    }

    override fun logoutUsuario() {
        viewModelScope.launch(Dispatchers.IO) {
            usuarioHelper.logout()
        }
    }
}