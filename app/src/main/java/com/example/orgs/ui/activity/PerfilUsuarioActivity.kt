package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.databinding.ActivityPerfilUsuarioBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PerfilUsuarioActivity : UsuariosBaseActivity() {
    private val binding: ActivityPerfilUsuarioBinding by lazy {
        ActivityPerfilUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // Configura componentes da tela
        title = getString(R.string.perfil_usuario_title)

        bindUsuarioData()
        setUpLogoutButtonListener()
    }

    private fun bindUsuarioData() {
        lifecycleScope.launch {
            usuario
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
                logout()
            }
        }
    }
}