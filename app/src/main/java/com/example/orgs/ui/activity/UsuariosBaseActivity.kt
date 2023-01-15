package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.repositories.UsuariosRepository
import com.example.orgs.extensions.navigateTo
import com.example.orgs.model.Usuario
import com.example.orgs.preferences.UsuariosPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class UsuariosBaseActivity : AppCompatActivity() {
    private val _usuario = MutableStateFlow<Usuario?>(null)   // Estado mutável
    protected val usuario: StateFlow<Usuario?> = _usuario          // Observable

    private val usuariosRepository by lazy { UsuariosRepository(context = this) }
    private val usuariosPreferences by lazy { UsuariosPreferences(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            verifyUsuarioLogged()
        }
    }

    private suspend fun verifyUsuarioLogged() {
        usuariosPreferences.watchUsuarioName().collect { usuarioNameStored ->
            usuarioNameStored?.let { usuarioName ->
                tryFindUsuarioInDatabase(usuarioName)
            } ?: navigateToLogin()
        }
    }

    private suspend fun tryFindUsuarioInDatabase(usuarioName: String): Usuario? {
        return usuariosRepository
            .findByNameId(usuarioName)
            .firstOrNull().also {
                _usuario.value = it
            }
    }

    protected suspend fun logout() {
        usuariosPreferences.removeUsuarioName()
    }

    private fun navigateToLogin() {
        navigateTo(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }
}