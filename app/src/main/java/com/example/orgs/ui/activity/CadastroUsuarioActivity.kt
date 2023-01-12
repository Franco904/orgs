package com.example.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.repositories.UsuariosRepository
import com.example.orgs.databinding.ActivityCadastroUsuarioBinding
import com.example.orgs.extensions.showToast
import com.example.orgs.extensions.toHash
import com.example.orgs.model.Usuario
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CadastroUsuarioActivity : AppCompatActivity() {
    private val TAG = "CadastroUsuarioActivity"

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
            signUp()
        }
    }

    private fun signUp() {
        val usuario = createUsuario()
        val handler: CoroutineExceptionHandler = setCoroutineHandlerException(
            errorMessage = "Erro ao cadastrar usuário"
        )

        lifecycleScope.launch(handler) {
            repository.create(usuario)
        }

        finish()
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


    private fun setCoroutineHandlerException(errorMessage: String): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            Log.i(TAG, "throwable: $throwable")
            showToast(errorMessage)
        }
    }
}