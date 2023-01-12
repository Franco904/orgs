package com.example.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.constants.ID_DEFAULT
import com.example.orgs.constants.PRODUTO_ID_EXTRA
import com.example.orgs.database.repositories.ProdutosRepository
import com.example.orgs.databinding.ActivityCadastroProdutoBinding
import com.example.orgs.extensions.tryLoadImage
import com.example.orgs.model.Produto
import com.example.orgs.ui.widget.CadastroProdutoImageDialog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CadastroProdutoActivity : AppCompatActivity() {
    private var produtoToEditId: Long = ID_DEFAULT
    private var produtoToEdit: Produto? = null
    private var imageUrl: String? = null

    private val repository by lazy { ProdutosRepository(context = this) }

    private val binding by lazy {
        ActivityCadastroProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        title = getString(R.string.cadastrar_produto_title)

        getIntentData()
    }

    override fun onResume() {
        super.onResume()

        tryFindProdutoInDatabase()

        setUpOnSaveListener()
        setUpOnImageTappedListener()
    }

    private fun getIntentData() {
        produtoToEditId = intent.getLongExtra(PRODUTO_ID_EXTRA, ID_DEFAULT)
    }

    private fun tryFindProdutoInDatabase() {
        val handlerProdutoFind = setCoroutineExceptionHandler(
            errorMessage = "Erro ao tentar encontrar produto no banco de dados."
        )

        lifecycleScope.launch(handlerProdutoFind) {
            produtoToEdit = repository.findById(produtoToEditId)
            bindEditProdutoDataIfNeeded()
        }
    }

    private fun bindEditProdutoDataIfNeeded() {
        produtoToEdit?.let {
            title = getString(R.string.editar_produto_title)

            binding.apply {
                imageUrl = produtoToEdit?.imagemUrl
                cadastroProdutoItemImage.tryLoadImage(url = produtoToEdit?.imagemUrl)

                cadastroProdutoFieldTitulo.setText(produtoToEdit?.titulo)
                cadastroProdutoFieldDescricao.setText(produtoToEdit?.descricao)
                cadastroProdutoFieldValor.setText(produtoToEdit?.valor.toString())
            }
        }
    }

    private fun setUpOnSaveListener() {
        // Configura callback de salvamento do produto
        binding.cadastroProdutoBtnSalvar.setOnClickListener {
            val handlerProdutoSave = setCoroutineExceptionHandler(
                errorMessage = "Erro ao salvar produto no banco de dados."
            )

            lifecycleScope.launch(handlerProdutoSave) {
                repository.create(produto = createProduto())

                binding.cadastroProdutoBtnSalvar.isEnabled = false
                finish()
            }
        }
    }

    private fun setUpOnImageTappedListener() {
        // Configura callback para mostrar dialog de alteração de imagem
        binding.cadastroProdutoItemImage.setOnClickListener {
            val cadastroProdutoImageDialog = CadastroProdutoImageDialog(context = this)
            cadastroProdutoImageDialog.show(currentImageUrl = imageUrl) { newUrl ->
                imageUrl = newUrl

                // Carrega imagem da URL no topo da página
                binding.cadastroProdutoItemImage.tryLoadImage(url = newUrl)
            }
        }
    }

    private fun createProduto(): Produto {
        val tituloField = binding.cadastroProdutoFieldTitulo
        val titulo = tituloField.text.toString()

        val descricaoField = binding.cadastroProdutoFieldDescricao
        val descricao = descricaoField.text.toString()

        val valorField = binding.cadastroProdutoFieldValor
        val valor = valorField.text.toString().trim()

        val valorCasted = if (valor.isEmpty()) BigDecimal.ZERO else BigDecimal(valor)

        return Produto(
            id = produtoToEdit?.id,
            titulo = titulo,
            descricao = descricao,
            valor = valorCasted,
            imagemUrl = imageUrl,
        )
    }

    private fun setCoroutineExceptionHandler(errorMessage: String? = null): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            Log.i("CadastroProdutoActivity", "throwable: $throwable")
            Toast.makeText(
                this,
                errorMessage ?: "Ocorreu um erro durante a execução da coroutine",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}
