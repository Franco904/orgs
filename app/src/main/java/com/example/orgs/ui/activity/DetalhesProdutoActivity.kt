package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.constants.PRODUTO_ID_DEFAULT
import com.example.orgs.constants.PRODUTO_ID_KEY
import com.example.orgs.database.repositories.ProdutosRepository
import com.example.orgs.databinding.ActivityDetalhesProdutoBinding
import com.example.orgs.extensions.tryLoadImage
import com.example.orgs.model.Produto
import com.example.orgs.ui.widget.ExcluirProdutoConfirmacaoDialog
import java.text.NumberFormat
import java.util.*

class DetalhesProdutoActivity : AppCompatActivity() {
    private var produtoId: Long = PRODUTO_ID_DEFAULT
    private var produto: Produto? = null

    private val repository by lazy { ProdutosRepository(context = this) }

    private val binding: ActivityDetalhesProdutoBinding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        supportActionBar?.hide()
        setContentView(binding.root)

        getIntentData()
        findProdutoInDatabase()

        if (produto != null) {
            bindProdutoData()

            setUpEditButtonListener()
            setUpDeleteButtonListener(produtoToDelete = produto!!)
        }
    }

    override fun onResume() {
        super.onResume()

        findProdutoInDatabase()
    }

    private fun getIntentData() {
        produtoId = intent.getLongExtra(PRODUTO_ID_KEY, PRODUTO_ID_DEFAULT)
    }

    private fun findProdutoInDatabase() {
        produto = repository.findById(produtoId)
        produto?.let {
            bindProdutoData()
        } ?: finish()
    }

    private fun bindProdutoData() {
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
                repository.delete(produtoToDelete)
                finish()
            }
        }
    }
}