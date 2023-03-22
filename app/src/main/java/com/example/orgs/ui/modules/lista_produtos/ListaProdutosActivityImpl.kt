package com.example.orgs.ui.modules.lista_produtos

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orgs.R
import com.example.orgs.contracts.ui.modules.lista_produtos.ListaProdutosActivity
import com.example.orgs.util.constants.ID_DEFAULT
import com.example.orgs.util.constants.PRODUTO_ID_EXTRA
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.data.enums.getOrderingPatternEnumByName
import com.example.orgs.data.enums.getProdutoFieldEnumByName
import com.example.orgs.util.extensions.navigateTo
import com.example.orgs.data.model.Produto
import com.example.orgs.ui.modules.perfil.PerfilUsuarioActivityImpl
import com.example.orgs.ui.modules.produtos_usuarios.ProdutosUsuariosActivityImpl
import com.example.orgs.ui.modules.cadastro_produto.CadastroProdutoActivityImpl
import com.example.orgs.ui.modules.detalhes_produto.DetalhesProdutoActivityImpl
import com.example.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import com.example.orgs.ui.widget.ExcluirProdutoConfirmacaoDialog
import com.example.orgs.ui.widget.ProdutoCardPopupMenu
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListaProdutosActivityImpl : AppCompatActivity(), ListaProdutosActivity {
    private val TAG = "ListaProdutosActivity"

    private val adapter by lazy { ListaProdutosAdapter(context = this) }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val viewModel: ListaProdutosViewModelImpl by viewModels()

    private val binding: ActivityListaProdutosBinding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // Configura componentes da tela
        title = getString(R.string.lista_produtos_title)

        lifecycleScope.launch {
            viewModel.produtos.collect { produtos ->
                updateProdutosList(produtos)
            }
        }

        lifecycleScope.launch {
            viewModel.produtos.filter { it.isNotEmpty() }.take(1).collect {
                setUpProdutoCardListeners()
            }
        }

        setUpRecyclerView()
        setUpFloatingActionButtonListener()
        setUpOrderingDropdowns()
    }

    override fun setUpRecyclerView() {
        val recyclerView = binding.listaProdutosRecyclerView

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    override fun setUpFloatingActionButtonListener() {
        val fab = binding.listaProdutosFab

        // Ao clicar no botão FAB, navegar para a tela de cadastro de produto
        fab.setOnClickListener {
            navigateToCadastroProduto()
        }
    }

    override fun setUpOrderingDropdowns() {
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

            viewModel.findProdutosOrderedByField(field, orderingPattern)
        }

        orderingFilterItem.setOnItemClickListener { _, _, position, _ ->
            val field = getProdutoFieldEnumByName(name = propertyFilterItem.text.toString())
            val orderingPattern = getOrderingPatternEnumByName(name = orderingValues[position])

            viewModel.findProdutosOrderedByField(field, orderingPattern)
        }
    }

    override fun updateProdutosList(produtos: List<Produto>) = adapter.updateAdapterState(produtos)

    override fun setUpProdutoCardListeners() {
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
                        viewModel.deleteProduto(produto)
                        null
                    }
                }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_produtos_actions, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_produtos_usuarios -> {
                navigateTo(ProdutosUsuariosActivityImpl::class.java)
            }
            R.id.action_perfil -> {
                navigateTo(PerfilUsuarioActivityImpl::class.java)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun navigateToCadastroProduto(produtoId: Long? = ID_DEFAULT) {
        navigateTo(CadastroProdutoActivityImpl::class.java) {
            putExtra(PRODUTO_ID_EXTRA, produtoId)
        }
    }

    private fun navigateToDetalhesProduto(produtoId: Long? = null) {
        navigateTo(DetalhesProdutoActivityImpl::class.java) {
            putExtra(PRODUTO_ID_EXTRA, produtoId)
        }
    }
}
