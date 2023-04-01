package com.example.orgs.ui.modules.cadastro_usuario

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.contracts.ui.modules.cadastro_usuario.CadastroUsuarioActivity
import com.example.orgs.data.model.Usuario
import com.example.orgs.databinding.ActivityCadastroUsuarioBinding
import com.example.orgs.util.extensions.toHash
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CadastroUsuarioActivityImpl : AppCompatActivity(), CadastroUsuarioActivity {
    private val viewModel: CadastroUsuarioViewModelImpl by viewModels()

    private val binding by lazy {
        ActivityCadastroUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setUpSignUpButtonListener()
    }

    override fun setUpSignUpButtonListener() {
        binding.cadastroUsuarioBtnCadastrar.setOnClickListener {
            signUp()
        }
    }

    override fun signUp() {
        val usuario = createUsuario()

        if (usuario.isValid) {
            viewModel.createUsuarioInDatabase(usuario)
            finish()
        }
    }

    override fun createUsuario(): Usuario {
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