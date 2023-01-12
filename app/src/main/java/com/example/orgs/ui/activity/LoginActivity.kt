package com.example.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.repositories.UsuariosRepository
import com.example.orgs.databinding.ActivityLoginBinding
import com.example.orgs.extensions.navigateTo
import com.example.orgs.extensions.showToast
import com.example.orgs.extensions.toHash
import com.example.orgs.preferences.UsuariosPreferences
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val repository by lazy { UsuariosRepository(context = this) }
    private val preferences by lazy { UsuariosPreferences(context = this) }

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setUpSignInButtonListener()
        setUpSignUpButtonListener()
    }

    private fun setUpSignInButtonListener() {
        binding.loginBtnEntrar.setOnClickListener {
            val usuarioName = binding.loginFieldUsuario.text.toString()
            val senha = binding.loginFieldSenha.text.toString().toHash()

            val exceptionHandler = setCoroutineExceptionHandler(
                errorMessage = "Erro ao efetuar autenticação do usuário",
            )

            lifecycleScope.launch(exceptionHandler) {
                repository.findByUserAndPassword(usuarioName, senha)?.let { usuario ->
                    preferences.writeUsuarioName(usuarioName = usuario.usuario)

                    navigateTo(ListaProdutosActivity::class.java)
                } ?: showToast("Usuário não encontrado")
            }
        }
    }

    private fun setUpSignUpButtonListener() {
        binding.loginBtnCadastrar.setOnClickListener {
            navigateTo(CadastroUsuarioActivity::class.java)
        }
    }

    private fun setCoroutineExceptionHandler(errorMessage: String): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            Log.i("LoginActivity", "throwable: $throwable")
            showToast(errorMessage)
        }
    }
}
