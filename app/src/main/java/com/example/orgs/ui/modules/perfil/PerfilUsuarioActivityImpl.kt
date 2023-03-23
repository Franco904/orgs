package com.example.orgs.ui.modules.perfil

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.contracts.ui.modules.perfil.PerfilUsuarioActivity
import com.example.orgs.contracts.ui.modules.perfil.PerfilUsuarioViewModel
import com.example.orgs.data.database.AppDatabase
import com.example.orgs.data.database.repositories.UsuariosRepositoryImpl
import com.example.orgs.databinding.ActivityPerfilUsuarioBinding
import com.example.orgs.infra.preferences.UsuariosPreferencesImpl
import com.example.orgs.ui.helper.UsuarioBaseHelperImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PerfilUsuarioActivityImpl : AppCompatActivity(), PerfilUsuarioActivity {
    private val viewModel: PerfilUsuarioViewModelImpl by viewModels()

    private val binding: ActivityPerfilUsuarioBinding by lazy {
        ActivityPerfilUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // Configura componentes da tela
        title = getString(R.string.perfil_usuario_title)

        lifecycleScope.launch {
            bindUsuarioData()
        }

        setUpLogoutButtonListener()
    }

    override suspend fun bindUsuarioData() {
        viewModel.usuario
            .filterNotNull()
            .collect { usuarioValue ->
                binding.perfilUsuarioUsuarioText.text = usuarioValue.usuario
                binding.perfilUsuarioNomeText.text = usuarioValue.nome
            }
    }

    override fun setUpLogoutButtonListener() {
        binding.perfilUsuarioBtnSair.setOnClickListener {
            viewModel.logoutUsuario()
            finish()
        }
    }
}