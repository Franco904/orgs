package com.example.orgs.ui.modules.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orgs.contracts.data.database.repositories.UsuariosRepository
import com.example.orgs.contracts.infra.preferences.UsuariosPreferences
import com.example.orgs.contracts.ui.modules.login.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val usuariosRepository: UsuariosRepository,
    private val usuariosPreferences: UsuariosPreferences,
) : ViewModel(), LoginViewModel {
    private val _isUsuarioLoggedIn = MutableStateFlow(false)
    override val isUsuarioLoggedIn: StateFlow<Boolean> = _isUsuarioLoggedIn

    private val _usuarioNotFound = MutableStateFlow(false)
    override val usuarioNotFound: StateFlow<Boolean> = _usuarioNotFound

    override fun login(usuarioName: String, senha: String) {
        viewModelScope.launch {
            usuariosRepository.findByUserAndPassword(usuarioName, senha)?.let { usuario ->
                usuariosPreferences.writeUsuarioName(usuarioName = usuario.usuario)

                _isUsuarioLoggedIn.value = true
            } ?: setUsuarioNotFound()
        }
    }

    private fun setUsuarioNotFound() {
        _usuarioNotFound.value = true
    }
}