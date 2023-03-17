package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.data.database.AppDatabase
import com.example.orgs.data.database.repositories.UsuariosRepositoryImpl
import com.example.orgs.databinding.ActivityPerfilUsuarioBinding
import com.example.orgs.infra.preferences.UsuariosPreferencesImpl
import com.example.orgs.ui.activity.helper.UsuarioBaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PerfilUsuarioActivity : AppCompatActivity() {
    private val usuariosRepository by lazy {
        UsuariosRepositoryImpl(
            dao = AppDatabase.getInstance(context = this).usuariosDao(),
        )
    }

    private val usuariosPreferences by lazy {
        UsuariosPreferencesImpl(context = this)
    }

    private val usuarioHelper by lazy {
        UsuarioBaseHelper(
            context = this,
            repository = usuariosRepository,
            preferences = usuariosPreferences,
        )
    }

    private val binding: ActivityPerfilUsuarioBinding by lazy {
        ActivityPerfilUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // Configura componentes da tela
        title = getString(R.string.perfil_usuario_title)

        lifecycleScope.launch(Dispatchers.IO) {
            usuarioHelper.verifyUsuarioLogged()
        }

        bindUsuarioData()
        setUpLogoutButtonListener()
    }

    private fun bindUsuarioData() {
        lifecycleScope.launch {
            usuarioHelper.usuario
                .filterNotNull()
                .collect { usuarioValue ->
                    binding.perfilUsuarioUsuarioText.text = usuarioValue.usuario
                    binding.perfilUsuarioNomeText.text = usuarioValue.nome
                }
        }
    }

    private fun setUpLogoutButtonListener() {
        binding.perfilUsuarioBtnSair.setOnClickListener {
            lifecycleScope.launch {
                finish()
                usuarioHelper.logout()
            }
        }
    }
}