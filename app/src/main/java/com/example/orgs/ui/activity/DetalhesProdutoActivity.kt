package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.databinding.ActivityDetalhesProdutoBinding
import com.example.orgs.extensions.tryLoadImage
import com.example.orgs.model.Produto
import java.text.NumberFormat
import java.util.*

class DetalhesProdutoActivity : AppCompatActivity() {
    private val binding: ActivityDetalhesProdutoBinding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }

    private lateinit var produto: Produto

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        supportActionBar?.hide()
        setContentView(binding.root)

        getIntentData()
        bindProdutoData()
    }

    private fun getIntentData() {
        intent?.let {
            val produtoRetrieved = it.getParcelableExtra<Produto>("produto")
            if (produtoRetrieved == null) {
                finish()
            }

            produto = produtoRetrieved!!
        }
    }

    private fun bindProdutoData() {
        binding.apply {
            produtoImage.tryLoadImage(produto.imagemUrl)

            val currencyFormatter: NumberFormat =
                NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            produtoValor.text = currencyFormatter.format(produto.valor)

            produtoTitulo.text = if (produto.titulo == "") "Sem nome definido" else produto.titulo
            produtoDescricao.text =
                if (produto.descricao == "") "Sem descrição" else produto.descricao
        }
    }
}