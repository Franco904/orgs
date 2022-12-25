package com.example.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.orgs.dao.ProdutosDao
import com.example.orgs.databinding.ActivityCadastroProdutoBinding
import com.example.orgs.databinding.ProdutoFormDialogBinding
import com.example.orgs.model.Produto
import java.math.BigDecimal

class CadastroProdutoActivity : AppCompatActivity() {
    private val dao get() = ProdutosDao()

    private val binding by lazy {
        ActivityCadastroProdutoBinding.inflate(layoutInflater)
    }

    private var imageUrl: String? = null

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
            val bindingProdutoImage = ProdutoFormDialogBinding.inflate(layoutInflater)

            // Carrega imagem atual ao abrir dialog
            if (imageUrl != null && imageUrl != "") {
                bindingProdutoImage.updateImageItemImage.load(imageUrl)
            }

            bindingProdutoImage.updateImageBtnCarregar.setOnClickListener {
                val url = bindingProdutoImage.updateImageFieldUrl.editText?.text.toString()
                // Carrega imagem da URL dentro da dialog
                bindingProdutoImage.updateImageItemImage.load(url)
            }

            AlertDialog.Builder(this)
                .setView(bindingProdutoImage.root)
                .setPositiveButton("Confirmar") { _, _ ->
                    if (imageUrl != null && imageUrl != "") {
                        imageUrl = bindingProdutoImage.updateImageFieldUrl.editText?.text.toString()
                        // Carrega imagem da URL no topo da página
                        binding.cadastroProdutoItemImage.load(imageUrl)
                    }
                }
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

        return Produto(titulo, descricao, valorCasted, imageUrl)
    }

    private fun persistProduto(produto: Produto) {
        dao.create(produto)
    }
}
