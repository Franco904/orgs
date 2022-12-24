package com.example.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.R
import com.example.orgs.dao.ProdutosDao
import com.example.orgs.databinding.ActivityCadastroProdutoBinding
import com.example.orgs.model.Produto
import java.math.BigDecimal

class CadastroProdutoActivity : AppCompatActivity() {
    private val dao get() = ProdutosDao()

    private val binding by lazy {
        ActivityCadastroProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // Configura callback de salvamento do produto
        binding.cadastroProdutoBtnSalvar.setOnClickListener {
            val produto = createProduto()
            persistProduto(produto)

            Log.i("CadastroProduto", "onCreate: $produto")
            Log.i("CadastroProduto", "onCreate: ${dao.findAll()}")

            finish()
        }

        // Configura callback para mostrar dialog de alteração de imagem
        binding.cadastroProdutoItemImage.setOnClickListener {
            AlertDialog.Builder(this)
                .setView(R.layout.produto_form_dialog)
                .setPositiveButton("Confirmar") { _, _ -> }
                .setNegativeButton("Cancelar") { _, _ -> }
                .show()
        }
    }

    private fun createProduto(): Produto {
        val tituloField = binding.cadastroProdutoFieldTitulo
        val titulo = tituloField.editText?.text.toString()

        val descricaoField = binding.cadastroProdutoFieldDescricao
        val descricao = descricaoField.editText?.text.toString()

        val valorField = binding.cadastroProdutoFieldValor
        val valor = valorField.editText?.text.toString().trim()

        val valorCasted = if (valor.isEmpty()) BigDecimal.ZERO else BigDecimal(valor)

        return Produto(titulo, descricao, valorCasted)
    }

    private fun persistProduto(produto: Produto) {
        dao.create(produto)
    }
}
