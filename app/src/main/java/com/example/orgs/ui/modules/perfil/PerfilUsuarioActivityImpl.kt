package com.example.orgs.ui.modules.perfil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.contracts.ui.modules.perfil.PerfilUsuarioActivity
import com.example.orgs.data.database.AppDatabase
import com.example.orgs.data.database.repositories.UsuariosRepositoryImpl
import com.example.orgs.databinding.ActivityPerfilUsuarioBinding
import com.example.orgs.infra.preferences.UsuariosPreferencesImpl
import com.example.orgs.ui.helper.UsuarioBaseHelperImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilUsuarioActivityImpl : AppCompatActivity(), PerfilUsuarioActivity {
    private val usuariosRepository by lazy {
        UsuariosRepositoryImpl(
            dao = AppDatabase.getInstance(context = this).usuariosDao(),
        )
    }

    private val usuariosPreferences by lazy {
        UsuariosPreferencesImpl(context = this)
    }

    private val usuarioHelper by lazy {
        UsuarioBaseHelperImpl(
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

        lifecycleScope.launch(Dispatchers.IO) {
            bindUsuarioData()
        }

        setUpLogoutButtonListener()
    }

    override suspend fun bindUsuarioData() {
        usuarioHelper.usuario
            .filterNotNull()
            .collect { usuarioValue ->
                binding.perfilUsuarioUsuarioText.text = usuarioValue.usuario
                binding.perfilUsuarioNomeText.text = usuarioValue.nome
            }
    }

    override fun setUpLogoutButtonListener() {
        binding.perfilUsuarioBtnSair.setOnClickListener {
            lifecycleScope.launch {
                finish()
                usuarioHelper.logout()
            }
        }
    }
}