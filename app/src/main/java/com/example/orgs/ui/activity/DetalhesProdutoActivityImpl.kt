package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.contracts.ui.DetalhesProdutoActivity
import com.example.orgs.util.constants.ID_DEFAULT
import com.example.orgs.util.constants.PRODUTO_ID_EXTRA
import com.example.orgs.data.database.AppDatabase
import com.example.orgs.data.database.repositories.ProdutosRepositoryImpl
import com.example.orgs.databinding.ActivityDetalhesProdutoBinding
import com.example.orgs.util.extensions.setCoroutineExceptionHandler
import com.example.orgs.util.extensions.tryLoadImage
import com.example.orgs.data.model.Produto
import com.example.orgs.ui.widget.ExcluirProdutoConfirmacaoDialog
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class DetalhesProdutoActivityImpl : AppCompatActivity(), DetalhesProdutoActivity {
    private val TAG = "DetalhesProdutoActivity"

    private var produtoId: Long = ID_DEFAULT
    private var produto: Produto? = null

    private val repository by lazy {
        ProdutosRepositoryImpl(
            dao = AppDatabase.getInstance(this).produtosDao(),
        )
    }

    private val binding: ActivityDetalhesProdutoBinding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        supportActionBar?.hide()
        setContentView(binding.root)

        getIntentData()
    }

    override fun onResume() {
        super.onResume()

        tryFindProdutoInDatabase()
    }

    override fun getIntentData() {
        produtoId = intent.getLongExtra(PRODUTO_ID_EXTRA, ID_DEFAULT)
    }

    override fun tryFindProdutoInDatabase() {
        val handlerProdutoFind = setCoroutineExceptionHandler(
            errorMessage = "Erro ao encontrar produto no banco de dados.",
            from = TAG,
        )

        lifecycleScope.launch(handlerProdutoFind) {
            produto = repository.findById(produtoId)

            bindProdutoDataIfExist()
            setUpEditButtonListener()
            setUpDeleteButtonListener(produtoToDelete = produto!!)
        }
    }

    override fun bindProdutoDataIfExist() {
        produto?.let {
            binding.apply {
                produtoImage.tryLoadImage(url = produto?.imagemUrl)

                val currencyFormatter: NumberFormat =
                    NumberFormat.getCurrencyInstance(Locale("pt", "br"))
                produtoValor.text = currencyFormatter.format(produto?.valor)

                produtoTitulo.text =
                    if (produto?.titulo == null || produto?.titulo == "") "Sem nome definido" else produto?.titulo
                produtoDescricao.text =
                    if (produto?.titulo == null || produto?.descricao == "") "Sem descrição" else produto?.descricao
            }
        } ?: finish()
    }

    override fun setUpEditButtonListener() {
        binding.editActionCard.setOnClickListener {
            Intent(this, CadastroProdutoActivityImpl::class.java).apply {
                putExtra(PRODUTO_ID_EXTRA, produto?.id)
                startActivity(this)
            }
        }
    }

    override fun setUpDeleteButtonListener(produtoToDelete: Produto) {
        binding.excludeActionCard.setOnClickListener {
            ExcluirProdutoConfirmacaoDialog(context = this).show {
                val handlerExcluirProduto = setCoroutineExceptionHandler(
                    errorMessage = "Erro ao excluir produto.",
                    from = TAG,
                )

                lifecycleScope.launch(handlerExcluirProduto) {
                    repository.delete(produtoToDelete)
                    finish()
                }

                null
            }
        }
    }
}