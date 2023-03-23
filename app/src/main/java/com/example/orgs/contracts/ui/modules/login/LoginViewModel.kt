package com.example.orgs.contracts.ui.modules.login

import kotlinx.coroutines.flow.StateFlow

interface LoginViewModel {
    val isUsuarioLoggedIn: StateFlow<Boolean>
    val usuarioNotFound: StateFlow<Boolean>

    fun login(usuarioName: String, senha: String)
}