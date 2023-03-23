package com.example.orgs.ui.modules.cadastro_usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orgs.contracts.data.database.repositories.UsuariosRepository
import com.example.orgs.contracts.ui.modules.cadastro_usuario.CadastroUsuarioViewModel
import com.example.orgs.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CadastroUsuarioViewModelImpl @Inject constructor(
    private val usuariosRepository: UsuariosRepository,
) : ViewModel(), CadastroUsuarioViewModel {
    override fun createUsuarioInDatabase(usuario: Usuario) {
        viewModelScope.launch {
            usuariosRepository.create(usuario)
        }
    }
}