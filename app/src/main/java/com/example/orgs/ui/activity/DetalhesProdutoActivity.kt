package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.constants.PRODUTO_ID_DEFAULT
import com.example.orgs.constants.PRODUTO_ID_KEY
import com.example.orgs.database.repositories.ProdutosRepository
import com.example.orgs.databinding.ActivityDetalhesProdutoBinding
import com.example.orgs.extensions.tryLoadImage
import com.example.orgs.model.Produto
import com.example.orgs.ui.widget.ExcluirProdutoConfirmacaoDialog
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*

class DetalhesProdutoActivity : AppCompatActivity() {
    private var produtoId: Long = PRODUTO_ID_DEFAULT
    private var produto: Produto? = null

    private val coroutineScope by lazy { MainScope() }

    private val repository by lazy { ProdutosRepository(context = this) }

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
        produtoId = intent.getLongExtra(PRODUTO_ID_KEY, PRODUTO_ID_DEFAULT)
    }

    private fun tryFindProdutoInDatabase() {
        val handlerProdutoFind = setCoroutineExceptionHandler(
            errorMessage = "Erro ao encontrar produto no banco de dados."
        )

        coroutineScope.launch(handlerProdutoFind) {
            val produtoStored = withContext(Dispatchers.IO) {
                repository.findById(produtoId)
            }
            produto = produtoStored

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
                putExtra(PRODUTO_ID_KEY, produto?.id)
                startActivity(this)
            }
        }
    }

    private fun setUpDeleteButtonListener(produtoToDelete: Produto) {
        binding.excludeActionCard.setOnClickListener {
            ExcluirProdutoConfirmacaoDialog(context = this).show {
                val handlerExcluirProduto = setCoroutineExceptionHandler(
                    errorMessage = "Erro ao excluir produto."
                )

                coroutineScope.launch(handlerExcluirProduto) {
                    withContext(Dispatchers.IO) {
                        repository.delete(produtoToDelete)
                        repository.findAll()
                    }

                    finish()
                }

                null
            }
        }
    }

    private fun setCoroutineExceptionHandler(errorMessage: String? = null): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            Log.i("DetalhesProdutoActivity", "throwable: $throwable")
            Toast.makeText(
                this,
                errorMessage ?: "Ocorreu um erro durante a execução da coroutine",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}