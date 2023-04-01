package com.example.orgs.ui.modules.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.contracts.ui.modules.login.LoginActivity
import com.example.orgs.databinding.ActivityLoginBinding
import com.example.orgs.ui.modules.cadastro_usuario.CadastroUsuarioActivityImpl
import com.example.orgs.ui.modules.lista_produtos.ListaProdutosActivityImpl
import com.example.orgs.util.extensions.navigateTo
import com.example.orgs.util.extensions.showToast
import com.example.orgs.util.extensions.toHash
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivityImpl : AppCompatActivity(), LoginActivity {
    private val viewModel: LoginViewModelImpl by viewModels()

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setUpOnLoginListener()
        setUpOnLoginFailedListener()

        setUpSignInButtonListener()
        setUpSignUpButtonListener()
    }

    override fun setUpOnLoginListener() {
        lifecycleScope.launch {
            viewModel.isUsuarioLoggedIn.filter { it }.collect {
                navigateTo(ListaProdutosActivityImpl::class.java)
            }
        }
    }

    override fun setUpOnLoginFailedListener() {
        lifecycleScope.launch {
            viewModel.usuarioNotFound.filter { it }.collect {
                showToast("Usuário não encontrado")
            }
        }
    }

    override fun setUpSignInButtonListener() {
        binding.loginBtnEntrar.setOnClickListener {
            val usuarioName = binding.loginFieldUsuario.text.toString()
            val senha = binding.loginFieldSenha.text.toString().toHash()

            viewModel.login(usuarioName, senha)
        }
    }

    override fun setUpSignUpButtonListener() {
        binding.loginBtnCadastrar.setOnClickListener {
            navigateTo(CadastroUsuarioActivityImpl::class.java)
        }
    }
}
