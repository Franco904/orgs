package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.AppDatabase
import com.example.orgs.database.repositories.UsuariosRepository
import com.example.orgs.extensions.navigateTo
import com.example.orgs.model.Usuario
import com.example.orgs.model.UsuarioWithProdutos
import com.example.orgs.preferences.UsuariosPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class UsuariosBaseActivity : AppCompatActivity() {
    private val _usuario = MutableStateFlow<Usuario?>(null)   // Estado mutável
    protected val usuario: StateFlow<Usuario?> = _usuario          // Observable

    private val usuariosRepository by lazy {
        UsuariosRepository(
            dao = AppDatabase.getInstance(this).usuariosDao(),
        )
    }
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

    protected suspend fun findUsuariosWithProdutos(): Flow<List<UsuarioWithProdutos>> {
        return usuariosRepository.findAllWithProdutos()
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