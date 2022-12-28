package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orgs.R
import com.example.orgs.database.AppDatabase
import com.example.orgs.database.dao.ProdutosDao
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.model.Produto
import com.example.orgs.ui.recyclerview.adapter.ListaProdutosAdapter

class ListaProdutosActivity : AppCompatActivity() {
    private lateinit var dao: ProdutosDao
    private val adapter by lazy { ListaProdutosAdapter(context = this) }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val binding: ActivityListaProdutosBinding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        dao = AppDatabase.getInstance(this).produtosDao()

        setContentView(binding.root)
        title = getString(R.string.lista_produtos_title)

        // Configura componentes da tela
        setUpRecyclerView()
        setUpFloatingActionButtonListener()
        setUpProdutoCardListener()
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

    private fun navigateToCadastroProduto() {
        val intent = Intent(this, CadastroProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun setUpProdutoCardListener() {
        adapter.onProdutoItemSelected = { produto: Produto ->
            navigateToDetalhesProduto(produto)
        }
    }

    private fun navigateToDetalhesProduto(produto: Produto) {
        val intent = Intent(this, DetalhesProdutoActivity::class.java)

        intent.putExtra("produto", produto)
        startActivity(intent)
    }
}
