package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.constants.ID_DEFAULT
import com.example.orgs.constants.PRODUTO_ID_EXTRA
import com.example.orgs.database.AppDatabase
import com.example.orgs.database.repositories.ProdutosRepository
import com.example.orgs.databinding.ActivityDetalhesProdutoBinding
import com.example.orgs.extensions.setCoroutineExceptionHandler
import com.example.orgs.extensions.tryLoadImage
import com.example.orgs.model.Produto
import com.example.orgs.ui.widget.ExcluirProdutoConfirmacaoDialog
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class DetalhesProdutoActivity : AppCompatActivity() {
    private val TAG = "DetalhesProdutoActivity"

    private var produtoId: Long = ID_DEFAULT
    private var produto: Produto? = null

    private val repository by lazy {
        ProdutosRepository(
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

    private fun getIntentData() {
        produtoId = intent.getLongExtra(PRODUTO_ID_EXTRA, ID_DEFAULT)
    }

    private fun tryFindProdutoInDatabase() {
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

    private fun bindProdutoDataIfExist() {
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

    private fun setUpEditButtonListener() {
        binding.editActionCard.setOnClickListener {
            Intent(this, CadastroProdutoActivity::class.java).apply {
                putExtra(PRODUTO_ID_EXTRA, produto?.id)
                startActivity(this)
            }
        }
    }

    private fun setUpDeleteButtonListener(produtoToDelete: Produto) {
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