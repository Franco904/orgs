package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.data.database.AppDatabase
import com.example.orgs.data.database.repositories.UsuariosRepository
import com.example.orgs.databinding.ActivityCadastroUsuarioBinding
import com.example.orgs.util.extensions.setCoroutineExceptionHandler
import com.example.orgs.util.extensions.toHash
import com.example.orgs.data.model.Usuario
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CadastroUsuarioActivity : AppCompatActivity() {
    private val TAG = "CadastroUsuarioActivity"

    private val repository by lazy {
        UsuariosRepository(
            dao = AppDatabase.getInstance(this).usuariosDao(),
        )
    }

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
        val handler: CoroutineExceptionHandler = setCoroutineExceptionHandler(
            errorMessage = "Erro ao cadastrar usuário",
            from = TAG,
        )

        if (usuario.isValid) {
            lifecycleScope.launch(handler) {
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
            id = null,
            usuario = usuario,
            nome = nome,
            senha = senha,
        )
    }
}