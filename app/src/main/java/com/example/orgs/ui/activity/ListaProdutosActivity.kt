package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orgs.R
import com.example.orgs.constants.PRODUTO_ID_DEFAULT
import com.example.orgs.constants.PRODUTO_ID_KEY
import com.example.orgs.database.repositories.ProdutosRepository
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.enums.getOrderingPatternEnumByName
import com.example.orgs.enums.getProdutoFieldEnumByName
import com.example.orgs.model.Produto
import com.example.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import com.example.orgs.ui.widget.ExcluirProdutoConfirmacaoDialog
import com.example.orgs.ui.widget.ProdutoCardPopupMenu

class ListaProdutosActivity : AppCompatActivity() {
    private val adapter by lazy { ListaProdutosAdapter(context = this) }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val repository by lazy { ProdutosRepository(context = this) }

    private val binding: ActivityListaProdutosBinding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        setContentView(binding.root)
        title = getString(R.string.lista_produtos_title)

        // Configura componentes da tela
        setUpFilterDropdowns()
        setUpRecyclerView()
        setUpFloatingActionButtonListener()
        setUpProdutoCardListeners()
    }

    override fun onResume() {
        super.onResume()

        // Cada vez que a activity assumir o primeiro plano novamente, atualizar a lista de produtos no adapter
        updateProdutosList(produtos = repository.findAll())
    }

    private fun setUpFilterDropdowns() {
        val propertyValues = resources.getStringArray(R.array.property_filter_options)
        val orderingValues = resources.getStringArray(R.array.ordering_filter_options)

        val arrayAdapterProperties = ArrayAdapter(this, R.layout.filter_item, propertyValues)
        val arrayAdapterOrdering = ArrayAdapter(this, R.layout.filter_item, orderingValues)

        val propertyFilterItem = binding.listaProdutosPropertyFilterDropdown
        val orderingFilterItem = binding.listaProdutosOrderingFilterDropdown

        propertyFilterItem.setAdapter(arrayAdapterProperties)
        orderingFilterItem.setAdapter(arrayAdapterOrdering)

        propertyFilterItem.setOnItemClickListener { _, _, position, _ ->
            val field = getProdutoFieldEnumByName(name = propertyValues[position])
            val orderingPattern =
                getOrderingPatternEnumByName(name = orderingFilterItem.text.toString())

            updateProdutosList(produtos = repository.findAllOrderedByField(field, orderingPattern))
        }

        orderingFilterItem.setOnItemClickListener { _, _, position, _ ->
            val field = getProdutoFieldEnumByName(name = propertyFilterItem.text.toString())
            val orderingPattern = getOrderingPatternEnumByName(name = orderingValues[position])

            updateProdutosList(produtos = repository.findAllOrderedByField(field, orderingPattern))
        }
    }

    private fun updateProdutosList(produtos: List<Produto>) = adapter.updateAdapterState(produtos)

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
            navigateToDetalhesProduto(produtoId = produto.id)
        }

        adapter.onProdutoItemLongClick = { cardView: View, produto: Produto ->
            val popupMenu = ProdutoCardPopupMenu(context = this, cardView)

            popupMenu.show(
                onEditDelegate = {
                    navigateToCadastroProduto(produtoId = produto.id)
                },
                onExcludeDelegate = {
                    ExcluirProdutoConfirmacaoDialog(context = this).show {
                        repository.delete(produto)
                        adapter.updateAdapterState(produtos = repository.findAll())
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
