package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
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
import kotlinx.coroutines.*

class ListaProdutosActivity : AppCompatActivity() {
    private val adapter by lazy { ListaProdutosAdapter(context = this) }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val coroutineScope by lazy { MainScope() }

    private val repository by lazy { ProdutosRepository(context = this) }

    private val binding: ActivityListaProdutosBinding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        setContentView(binding.root)

        // Configura componentes da tela
        title = getString(R.string.lista_produtos_title)

        setUpFilterDropdowns()
        setUpRecyclerView()
        setUpFloatingActionButtonListener()
    }

    override fun onResume() {
        super.onResume()

        val handler = setCoroutineExceptionHandler(
            errorMessage = "Erro ao buscar produtos no banco de dados para listagem.",
        )

        coroutineScope.launch(handler) {
            // Tudo dentro desse escopo é executado de maneira paralela
            // e não trava a thread principal
            val produtos = withContext(Dispatchers.IO) {
                // Executado em uma thread separada e criada para isso
                repository.findAll()
            }

            updateProdutosList(produtos)
            setUpProdutoCardListeners()
        }
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

            val handlerProperty = setCoroutineExceptionHandler(
                errorMessage = "Erro ao alterar filtragem por atributo de produto."
            )

            coroutineScope.launch(handlerProperty) {
                val produtos = withContext(Dispatchers.IO) {
                    repository.findAllOrderedByField(field, orderingPattern)
                }

                updateProdutosList(produtos)
            }
        }

        orderingFilterItem.setOnItemClickListener { _, _, position, _ ->
            val field = getProdutoFieldEnumByName(name = propertyFilterItem.text.toString())
            val orderingPattern = getOrderingPatternEnumByName(name = orderingValues[position])

            val handlerOrderingPattern = setCoroutineExceptionHandler(
                errorMessage = "Erro ao alterar filtragem por padrão de ordenação."
            )

            coroutineScope.launch(handlerOrderingPattern) {
                val produtos = withContext(Dispatchers.IO) {
                    repository.findAllOrderedByField(field, orderingPattern)
                }

                updateProdutosList(produtos)
            }
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
                        val handlerExcluirProduto = setCoroutineExceptionHandler(
                            errorMessage = "Erro ao excluir produto."
                        )

                        coroutineScope.launch(handlerExcluirProduto) {
                            val produtos = withContext(Dispatchers.IO) {
                                repository.delete(produto)
                                repository.findAll() // Returned
                            }

                            updateProdutosList(produtos)
                        }

                        null
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

    private fun setCoroutineExceptionHandler(errorMessage: String? = null): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            Log.i("ListaProdutosActivity", "throwable: $throwable")
            Toast.makeText(
                this,
                errorMessage ?: "Ocorreu um erro durante a execução da coroutine",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}
