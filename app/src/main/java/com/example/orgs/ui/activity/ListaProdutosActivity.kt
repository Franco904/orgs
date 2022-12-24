package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orgs.R
import com.example.orgs.dao.ProdutosDao
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.ui.recyclerview.adapter.ListaProdutosAdapter

class ListaProdutosActivity : AppCompatActivity() {
    private val dao = ProdutosDao()
    private val adapter by lazy { ListaProdutosAdapter(context = this, produtos = dao.findAll()) }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val binding: ActivityListaProdutosBinding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        setContentView(binding.root)

        // Configura componentes da tela
        setUpRecyclerView()
        setUpFloatingActionButton()
    }

    override fun onResume() {
        super.onResume()

        // Cada vez que a activity assumir o primeiro plano novamente, atualizar a lista de produtos no adapter
        adapter.updateAdapterState(dao.findAll())
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.listaProdutosRecyclerView

        Log.i("ListaProdutos", "onCreate: ${dao.findAll()}")

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    private fun setUpFloatingActionButton() {
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
}
