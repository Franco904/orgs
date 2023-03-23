package com.example.orgs.ui.modules.cadastro_produto

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.contracts.ui.modules.cadastro_produto.CadastroProdutoActivity
import com.example.orgs.databinding.ActivityCadastroProdutoBinding
import com.example.orgs.util.extensions.setCoroutineExceptionHandler
import com.example.orgs.util.extensions.tryLoadImage
import com.example.orgs.data.model.Produto
import com.example.orgs.ui.widget.CadastroProdutoImageDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.math.BigDecimal

@AndroidEntryPoint
class CadastroProdutoActivityImpl : AppCompatActivity(), CadastroProdutoActivity {
    private var imageUrl: String? = null

    private val viewModel: CadastroProdutoViewModelImpl by viewModels()

    private val binding by lazy {
        ActivityCadastroProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        title = getString(R.string.cadastrar_produto_title)

        lifecycleScope.launch {
            viewModel.produtoToEdit.filterNotNull().take(1).collect { produto ->
                bindEditProdutoDataIfNeeded(produto)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.tryFindProdutoInDatabase()

        setUpOnSaveListener()
        setUpOnImageTappedListener()
    }

    override fun bindEditProdutoDataIfNeeded(produto: Produto) {
        title = getString(R.string.editar_produto_title)

        binding.apply {
            imageUrl = produto.imagemUrl
            cadastroProdutoItemImage.tryLoadImage(url = produto.imagemUrl)

            cadastroProdutoFieldTitulo.setText(produto.titulo)
            cadastroProdutoFieldDescricao.setText(produto.descricao)
            cadastroProdutoFieldValor.setText(produto.valor.toDouble().toString())
        }
    }

    override fun setUpOnSaveListener() {
        // Configura callback de salvamento do produto
        binding.cadastroProdutoBtnSalvar.setOnClickListener {
            viewModel.usuario.value?.let { usuario ->
                val produto = createProduto(usuario.id)

                if (produto.isValid) {
                    viewModel.createProdutoInDatabase(produto)

                    binding.cadastroProdutoBtnSalvar.isEnabled = false
                    finish()
                }
            }
        }
    }

    override fun setUpOnImageTappedListener() {
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

    override fun createProduto(usuarioId: Long?): Produto {
        val tituloField = binding.cadastroProdutoFieldTitulo
        val titulo = tituloField.text.toString()

        val descricaoField = binding.cadastroProdutoFieldDescricao
        val descricao = descricaoField.text.toString()

        val valorField = binding.cadastroProdutoFieldValor
        val valor = valorField.text.toString().trim()

        val valorCasted = if (valor.isEmpty()) BigDecimal.ZERO else BigDecimal(valor)

        return Produto(
            id = viewModel.produtoToEdit.value?.id,
            titulo = titulo,
            descricao = descricao,
            valor = valorCasted,
            imagemUrl = imageUrl,
            usuarioId = usuarioId,
        )
    }
}
