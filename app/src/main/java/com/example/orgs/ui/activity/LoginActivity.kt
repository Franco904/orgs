package com.example.orgs.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.AppDatabase
import com.example.orgs.database.repositories.UsuariosRepository
import com.example.orgs.databinding.ActivityLoginBinding
import com.example.orgs.extensions.navigateTo
import com.example.orgs.extensions.setCoroutineExceptionHandler
import com.example.orgs.extensions.showToast
import com.example.orgs.extensions.toHash
import com.example.orgs.preferences.UsuariosPreferences
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    private val repository by lazy {
        UsuariosRepository(
            dao = AppDatabase.getInstance(this).usuariosDao(),
        )
    }
    private val preferences by lazy {
        UsuariosPreferences(
            context = this,
        )
    }

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

            login(usuarioName, senha)
        }
    }

    private fun setUpSignUpButtonListener() {
        binding.loginBtnCadastrar.setOnClickListener {
            navigateTo(CadastroUsuarioActivity::class.java)
        }
    }

    private fun login(usuarioName: String, senha: String) {
        val exceptionHandler = setCoroutineExceptionHandler(
            errorMessage = "Erro ao efetuar autenticação do usuário",
            from = TAG,
        )

        lifecycleScope.launch(exceptionHandler) {
            repository.findByUserAndPassword(usuarioName, senha)?.let { usuario ->
                preferences.writeUsuarioName(usuarioName = usuario.usuario)

                navigateTo(ListaProdutosActivity::class.java)
            } ?: showToast("Usuário não encontrado")
        }
    }
}
