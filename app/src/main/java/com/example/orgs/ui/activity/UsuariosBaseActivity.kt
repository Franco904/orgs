package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.data.database.AppDatabase
import com.example.orgs.data.database.repositories.UsuariosRepositoryImpl
import com.example.orgs.util.extensions.navigateTo
import com.example.orgs.data.model.Usuario
import com.example.orgs.data.model.UsuarioWithProdutos
import com.example.orgs.infra.preferences.UsuariosPreferencesImpl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class UsuariosBaseActivity : AppCompatActivity() {
    private val _usuario = MutableStateFlow<Usuario?>(null)   // Estado mutável
    protected val usuario: StateFlow<Usuario?> = _usuario          // Observable

    private val usuariosRepository by lazy {
        UsuariosRepositoryImpl(
            dao = AppDatabase.getInstance(this).usuariosDao(),
        )
    }
    private val usuariosPreferences by lazy { UsuariosPreferencesImpl(context = this) }

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

    protected fun findUsuariosWithProdutos(): Flow<List<UsuarioWithProdutos>> {
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