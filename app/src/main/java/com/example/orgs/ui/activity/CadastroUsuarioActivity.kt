package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.repositories.UsuariosRepository
import com.example.orgs.databinding.ActivityCadastroUsuarioBinding
import com.example.orgs.extensions.toHash
import com.example.orgs.model.Usuario
import kotlinx.coroutines.launch

class CadastroUsuarioActivity : AppCompatActivity() {
    private val repository by lazy { UsuariosRepository(context = this) }

    private val binding by lazy {
        ActivityCadastroUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setUpSignUpButtonListener()
    }

    private fun setUpSignUpButtonListener() {
        binding.cadastroProdutoBtnCadastrar.setOnClickListener {
            val usuario = createUsuario()

            lifecycleScope.launch {
                repository.create(usuario)
            }

            finish()
        }
    }

    private fun createUsuario(): Usuario {
        val usuario = binding.cadastroUsuarioFieldUsuario.text.toString()
        val nome = binding.cadastroUsuarioFieldNome.text.toString()
        val senha = binding.cadastroUsuarioFieldSenha.text.toString().toHash()

        return Usuario(
            usuario = usuario,
            nome = nome,
            senha = senha,
        )
    }
}