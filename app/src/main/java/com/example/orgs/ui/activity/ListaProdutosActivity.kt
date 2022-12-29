package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orgs.R
import com.example.orgs.constants.PRODUTO_ID_DEFAULT
import com.example.orgs.constants.PRODUTO_ID_KEY
import com.example.orgs.database.AppDatabase
import com.example.orgs.database.dao.ProdutosDao
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.model.Produto
import com.example.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import com.example.orgs.ui.widget.ExcluirProdutoConfirmacaoDialog
import com.example.orgs.ui.widget.ProdutoCardPopupMenu

class ListaProdutosActivity : AppCompatActivity() {
    private val dao: ProdutosDao by lazy { AppDatabase.getInstance(this).produtosDao() }
    private val adapter by lazy { ListaProdutosAdapter(context = this) }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val binding: ActivityListaProdutosBinding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        setContentView(binding.root)
        title = getString(R.string.lista_produtos_title)

        // Configura componentes da tela
        setUpRecyclerView()
        setUpFloatingActionButtonListener()
        setUpProdutoCardListeners()
    }

    override fun onResume() {
        super.onResume()

        // Cada vez que a activity assumir o primeiro plano novamente, atualizar a lista de produtos no adapter
        adapter.updateAdapterState(dao.findAll())
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.listaProdutosRecyclerView

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    private fun setUpFloatingActionButtonListener() {
        val fab = binding.listaProdutosFab

        // Ao clicar no botão FAB, navegar para a tela de cadastro de produto
        fab.setOnClickListener {
            navigateToCadastroProduto()
        }
    }

    private fun setUpProdutoCardListeners() {
        adapter.onProdutoItemClick = { produto: Produto ->
            navigateToDetalhesProduto(produto.id)
        }

        adapter.onProdutoItemLongClick = { cardView: View, produto: Produto ->
            val popupMenu = ProdutoCardPopupMenu(this, cardView)

            popupMenu.show(
                onEditDelegate = {
                    navigateToCadastroProduto(produto.id)
                },
                onExcludeDelegate = {
                    ExcluirProdutoConfirmacaoDialog(this).show {
                        dao.delete(produto)
                        adapter.updateAdapterState(dao.findAll())
                    }
                }
            )
        }
    }

    private fun navigateToCadastroProduto(produtoId: Long? = PRODUTO_ID_DEFAULT) {
        Intent(this, CadastroProdutoActivity::class.java).apply {
            putExtra(PRODUTO_ID_KEY, produtoId)
            startActivity(this)
        }
    }

    private fun navigateToDetalhesProduto(produtoId: Long? = null) {
        Intent(this, DetalhesProdutoActivity::class.java).apply {
            putExtra(PRODUTO_ID_KEY, produtoId)
            startActivity(this)
        }
    }
}
