package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.example.orgs.databinding.ActivityProdutosUsuariosBinding
import com.example.orgs.data.model.UsuarioWithProdutos
import com.example.orgs.ui.recyclerview.adapter.ProdutosConcatAdapter
import com.example.orgs.ui.recyclerview.adapter.UsuarioConcatAdapter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProdutosUsuariosActivity : UsuariosBaseActivity() {
    private val binding: ActivityProdutosUsuariosBinding by lazy {
        ActivityProdutosUsuariosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        lifecycleScope.launch {
            findUsuariosWithProdutos()
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