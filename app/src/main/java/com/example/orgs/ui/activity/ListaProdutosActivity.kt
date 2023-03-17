package com.example.orgs.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orgs.R
import com.example.orgs.util.constants.ID_DEFAULT
import com.example.orgs.util.constants.PRODUTO_ID_EXTRA
import com.example.orgs.data.database.AppDatabase
import com.example.orgs.data.database.repositories.ProdutosRepositoryImpl
import com.example.orgs.data.database.repositories.UsuariosRepositoryImpl
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.data.enums.getOrderingPatternEnumByName
import com.example.orgs.data.enums.getProdutoFieldEnumByName
import com.example.orgs.util.extensions.navigateTo
import com.example.orgs.util.extensions.setCoroutineExceptionHandler
import com.example.orgs.data.model.Produto
import com.example.orgs.infra.preferences.UsuariosPreferencesImpl
import com.example.orgs.ui.activity.helper.UsuarioBaseHelper
import com.example.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import com.example.orgs.ui.widget.ExcluirProdutoConfirmacaoDialog
import com.example.orgs.ui.widget.ProdutoCardPopupMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ListaProdutosActivity : AppCompatActivity() {
    private val TAG = "ListaProdutosActivity"

    private val adapter by lazy { ListaProdutosAdapter(context = this) }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val produtosRepository by lazy {
        ProdutosRepositoryImpl(
            dao = AppDatabase.getInstance(context = this).produtosDao(),
        )
    }

    private val usuariosRepository by lazy {
        UsuariosRepositoryImpl(
            dao = AppDatabase.getInstance(context = this).usuariosDao(),
        )
    }

    private val usuariosPreferences by lazy {
        UsuariosPreferencesImpl(context = this)
    }

    private val usuarioHelper by lazy {
        UsuarioBaseHelper(
            context = this,
            repository = usuariosRepository,
            preferences = usuariosPreferences,
        )
    }

    private val binding: ActivityListaProdutosBinding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // Configura componentes da tela
        title = getString(R.string.lista_produtos_title)

        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                usuarioHelper.verifyUsuarioLogged()
            }
        }

        setUpRecyclerView()
        setUpFloatingActionButtonListener()
        setUpOrderingDropdowns()

        setUpUsuarioStateListener()
    }

    private fun setUpOrderingDropdowns() {
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
                errorMessage = "Erro ao alterar filtragem por atributo de produto.",
                from = TAG,
            )

            lifecycleScope.launch(handlerProperty) {
                val produtos = produtosRepository.findAllOrderedByField(field, orderingPattern)

                updateProdutosList(produtos)
            }
        }

        orderingFilterItem.setOnItemClickListener { _, _, position, _ ->
            val field = getProdutoFieldEnumByName(name = propertyFilterItem.text.toString())
            val orderingPattern = getOrderingPatternEnumByName(name = orderingValues[position])

            val handlerOrderingPattern = setCoroutineExceptionHandler(
                errorMessage = "Erro ao alterar filtragem por padrão de ordenação.",
                from = TAG,
            )

            lifecycleScope.launch(handlerOrderingPattern) {
                val produtos = produtosRepository.findAllOrderedByField(field, orderingPattern)

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

    private fun setUpUsuarioStateListener() {
        val handler = setCoroutineExceptionHandler(
            errorMessage = "Erro ao escutar estado do usuário",
            from = TAG,
        )

        lifecycleScope.launch(handler) {
            // Escuta mudanças de estado na variável reativa (Flow)
            usuarioHelper.usuario
                .filterNotNull()
                .collect { usuario ->
                    getProdutosAndNotifyListeners(usuarioId = usuario.id!!)
                }
        }
    }

    private fun getProdutosAndNotifyListeners(usuarioId: Long) {
        val handlerFindProdutos = setCoroutineExceptionHandler(
            errorMessage = "Erro ao buscar produtos no banco de dados para listagem.",
            from = TAG,
        )

        lifecycleScope.launch(handlerFindProdutos) {
            produtosRepository.findAllByUsuarioId(usuarioId).collect {
                updateProdutosList(produtos = it)
                setUpProdutoCardListeners()
            }
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
                        deleteProduto(produto)
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
                navigateTo(ProdutosUsuariosActivity::class.java)
            }
            R.id.action_perfil -> {
                navigateTo(PerfilUsuarioActivity::class.java)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteProduto(produto: Produto) {
        val handlerExcluirProduto = setCoroutineExceptionHandler(
            errorMessage = "Erro ao excluir produto.",
            from = TAG,
        )

        lifecycleScope.launch(handlerExcluirProduto) {
            produtosRepository.delete(produto)
        }
    }

    private fun navigateToCadastroProduto(produtoId: Long? = ID_DEFAULT) {
        navigateTo(CadastroProdutoActivity::class.java) {
            putExtra(PRODUTO_ID_EXTRA, produtoId)
        }
    }

    private fun navigateToDetalhesProduto(produtoId: Long? = null) {
        navigateTo(DetalhesProdutoActivity::class.java) {
            putExtra(PRODUTO_ID_EXTRA, produtoId)
        }
    }
}


