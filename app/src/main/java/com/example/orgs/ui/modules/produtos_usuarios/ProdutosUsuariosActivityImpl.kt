package com.example.orgs.ui.modules.produtos_usuarios

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.example.orgs.contracts.ui.modules.produtos_usuarios.ProdutosUsuariosActivity
import com.example.orgs.data.database.AppDatabase
import com.example.orgs.data.database.repositories.UsuariosRepositoryImpl
import com.example.orgs.databinding.ActivityProdutosUsuariosBinding
import com.example.orgs.data.model.UsuarioWithProdutos
import com.example.orgs.infra.preferences.UsuariosPreferencesImpl
import com.example.orgs.ui.helper.UsuarioBaseHelperImpl
import com.example.orgs.ui.recyclerview.adapter.ProdutosConcatAdapter
import com.example.orgs.ui.recyclerview.adapter.UsuarioConcatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProdutosUsuariosActivityImpl : AppCompatActivity(), ProdutosUsuariosActivity {
    private val viewModel: ProdutosUsuariosViewModelImpl by viewModels()

    private val binding: ActivityProdutosUsuariosBinding by lazy {
        ActivityProdutosUsuariosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setUpRecyclerView()
    }

    override fun setUpRecyclerView() {
        lifecycleScope.launch {
            viewModel.usuariosWithProdutos
                .filterNotNull()
                .map { usuariosWithProdutos ->
                    usuariosWithProdutos
                        .map {
                            parseUsuarioWithProdutosToAdapter(usuarioWithProdutos = it)
                        }
                        .flatten() // Transforma sequência de listas em uma única (List<List<*>> -> List<*>)
                }.collect { adapters ->
                    binding.produtosUsuariosRecyclerView.adapter = ConcatAdapter(adapters)
                }
        }
    }

    private fun parseUsuarioWithProdutosToAdapter(usuarioWithProdutos: UsuarioWithProdutos) =
        listOf(
            UsuarioConcatAdapter().also { adapter ->
                adapter.submitList(listOf(usuarioWithProdutos.usuario))
            },
            ProdutosConcatAdapter().also { adapter ->
                adapter.submitList(usuarioWithProdutos.produtos)
            },
        )
}