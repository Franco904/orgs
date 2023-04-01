package com.example.orgs.ui.modules.perfil

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.contracts.ui.modules.perfil.PerfilUsuarioActivity
import com.example.orgs.databinding.ActivityPerfilUsuarioBinding
import com.example.orgs.ui.modules.login.LoginActivityImpl
import com.example.orgs.util.extensions.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

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

        setUpStateFlowListeners()

        setUpLogoutButtonListener()
    }

    private fun setUpStateFlowListeners() {
        lifecycleScope.launch {
            viewModel.hasSessionExpired.filter { it }.collect {
                navigateToLogin()
            }
        }

        lifecycleScope.launch {
            bindUsuarioData()
        }
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

    private fun navigateToLogin() {
        navigateTo(LoginActivityImpl::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }
}