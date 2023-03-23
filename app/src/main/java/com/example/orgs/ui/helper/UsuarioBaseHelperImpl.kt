package com.example.orgs.ui.helper

import android.content.Context
import android.content.Intent
import com.example.orgs.contracts.data.database.repositories.UsuariosRepository
import com.example.orgs.contracts.infra.preferences.UsuariosPreferences
import com.example.orgs.contracts.ui.helper.UsuarioBaseHelper
import com.example.orgs.data.model.Usuario
import com.example.orgs.data.model.UsuarioWithProdutos
import com.example.orgs.ui.modules.login.LoginActivityImpl
import com.example.orgs.util.extensions.navigateTo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UsuarioBaseHelperImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val repository: UsuariosRepository,
    private val preferences: UsuariosPreferences,
): UsuarioBaseHelper {
    private val _usuario = MutableStateFlow<Usuario?>(null) // Estado mutável
    override val usuario: StateFlow<Usuario?> = _usuario // Observable

    private val _usuariosWithProdutos = MutableStateFlow<List<UsuarioWithProdutos>?>(null)
    override val usuariosWithProdutos: StateFlow<List<UsuarioWithProdutos>?> = _usuariosWithProdutos

    override suspend fun verifyUsuarioLogged() {
        preferences.watchUsuarioName().collect { usuarioNameStored ->
            usuarioNameStored?.let { usuarioName ->
                tryFindUsuarioInDatabase(usuarioName)
            } ?: navigateToLogin()
        }
    }

    private suspend fun tryFindUsuarioInDatabase(usuarioName: String): Usuario? {
        return repository
            .findByNameId(usuarioName)
            .firstOrNull().also {
                _usuario.value = it
            }
    }

    override suspend fun findUsuariosWithProdutos(): List<UsuarioWithProdutos> {
        return repository.findAllWithProdutos()
    }

    override suspend fun logout() = preferences.removeUsuarioName()

    private fun navigateToLogin() {
        context.navigateTo(LoginActivityImpl::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}