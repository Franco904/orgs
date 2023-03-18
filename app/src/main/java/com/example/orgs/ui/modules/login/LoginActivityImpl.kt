package com.example.orgs.ui.modules.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.contracts.ui.modules.login.LoginActivity
import com.example.orgs.data.database.AppDatabase
import com.example.orgs.data.database.repositories.UsuariosRepositoryImpl
import com.example.orgs.databinding.ActivityLoginBinding
import com.example.orgs.util.extensions.navigateTo
import com.example.orgs.util.extensions.setCoroutineExceptionHandler
import com.example.orgs.util.extensions.showToast
import com.example.orgs.util.extensions.toHash
import com.example.orgs.infra.preferences.UsuariosPreferencesImpl
import com.example.orgs.ui.modules.cadastro_usuario.CadastroUsuarioActivityImpl
import com.example.orgs.ui.modules.lista_produtos.ListaProdutosActivityImpl
import kotlinx.coroutines.launch

class LoginActivityImpl : AppCompatActivity(), LoginActivity {
    private val TAG = "LoginActivity"

    private val repository by lazy {
        UsuariosRepositoryImpl(
            dao = AppDatabase.getInstance(this).usuariosDao(),
        )
    }
    private val preferences by lazy {
        UsuariosPreferencesImpl(
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

    override fun setUpSignInButtonListener() {
        binding.loginBtnEntrar.setOnClickListener {
            val usuarioName = binding.loginFieldUsuario.text.toString()
            val senha = binding.loginFieldSenha.text.toString().toHash()

            login(usuarioName, senha)
        }
    }

    override fun setUpSignUpButtonListener() {
        binding.loginBtnCadastrar.setOnClickListener {
            navigateTo(CadastroUsuarioActivityImpl::class.java)
        }
    }

    override fun login(usuarioName: String, senha: String) {
        val exceptionHandler = setCoroutineExceptionHandler(
            errorMessage = "Erro ao efetuar autenticação do usuário",
            from = TAG,
        )

        lifecycleScope.launch(exceptionHandler) {
            repository.findByUserAndPassword(usuarioName, senha)?.let { usuario ->
                preferences.writeUsuarioName(usuarioName = usuario.usuario)

                navigateTo(ListaProdutosActivityImpl::class.java)
            } ?: showToast("Usuário não encontrado")
        }
    }
}
