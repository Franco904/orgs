package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.R
import com.example.orgs.dao.ProdutosDao
import com.example.orgs.databinding.ActivityCadastroProdutoBinding
import com.example.orgs.extensions.tryLoadImage
import com.example.orgs.model.Produto
import com.example.orgs.ui.dialog.CadastroProdutoImageDialog
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
        title = getString(R.string.cadastrar_produto_title)

        setUpOnSave()
        setUpOnImageTapped()
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

    private fun setUpOnSave() {
        // Configura callback de salvamento do produto
        binding.cadastroProdutoBtnSalvar.setOnClickListener {
            val produto = createProduto()
            persistProduto(produto)

            finish()
        }
    }

    private fun persistProduto(produto: Produto) {
        dao.create(produto)
    }

    private fun setUpOnImageTapped() {
        // Configura callback para mostrar dialog de alteração de imagem
        binding.cadastroProdutoItemImage.setOnClickListener {
            val cadastroProdutoImageDialog = CadastroProdutoImageDialog(this)
            cadastroProdutoImageDialog.show(imageUrl) { newUrl ->
                imageUrl = newUrl

                // Carrega imagem da URL no topo da página
                binding.cadastroProdutoItemImage.tryLoadImage(newUrl)
            }
        }
    }
}
