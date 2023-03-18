package com.example.orgs.ui.modules.cadastro_produto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.contracts.ui.modules.cadastro_produto.CadastroProdutoActivity
import com.example.orgs.util.constants.ID_DEFAULT
import com.example.orgs.util.constants.PRODUTO_ID_EXTRA
import com.example.orgs.data.database.AppDatabase
import com.example.orgs.data.database.repositories.ProdutosRepositoryImpl
import com.example.orgs.data.database.repositories.UsuariosRepositoryImpl
import com.example.orgs.databinding.ActivityCadastroProdutoBinding
import com.example.orgs.util.extensions.setCoroutineExceptionHandler
import com.example.orgs.util.extensions.tryLoadImage
import com.example.orgs.data.model.Produto
import com.example.orgs.infra.preferences.UsuariosPreferencesImpl
import com.example.orgs.ui.helper.UsuarioBaseHelperImpl
import com.example.orgs.ui.widget.CadastroProdutoImageDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CadastroProdutoActivityImpl : AppCompatActivity(), CadastroProdutoActivity {
    private val TAG = "CadastroProdutoActivity"

    private var produtoToEditId: Long = ID_DEFAULT
    private var produtoToEdit: Produto? = null
    private var imageUrl: String? = null

    private val produtosRepository by lazy {
        ProdutosRepositoryImpl(
            dao = AppDatabase.getInstance(context = this).produtosDao(),
        )
    }

    private val usuariosRepository by lazy {
        UsuariosRepositoryImpl(
            dao = AppDatabase.getInstance(context = this).usuariosDao(),
        )
    }

    private val usuariosPreferences by lazy {
        UsuariosPreferencesImpl(context = this)
    }

    private val usuarioHelper by lazy {
        UsuarioBaseHelperImpl(
            context = this,
            repository = usuariosRepository,
            preferences = usuariosPreferences,
        )
    }

    private val binding by lazy {
        ActivityCadastroProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        title = getString(R.string.cadastrar_produto_title)

        lifecycleScope.launch(Dispatchers.IO) {
            usuarioHelper.verifyUsuarioLogged()
        }

        getIntentData()
    }

    override fun onResume() {
        super.onResume()

        tryFindProdutoInDatabase()

        setUpOnSaveListener()
        setUpOnImageTappedListener()
    }

    override fun getIntentData() {
        produtoToEditId = intent.getLongExtra(PRODUTO_ID_EXTRA, ID_DEFAULT)
    }

    override fun tryFindProdutoInDatabase() {
        val handlerProdutoFind = setCoroutineExceptionHandler(
            errorMessage = "Erro ao tentar encontrar produto no banco de dados.",
            from = TAG,
        )

        lifecycleScope.launch(handlerProdutoFind) {
            produtoToEdit = produtosRepository.findById(produtoToEditId)
            bindEditProdutoDataIfNeeded()
        }
    }

    override fun bindEditProdutoDataIfNeeded() {
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

    override fun setUpOnSaveListener() {
        // Configura callback de salvamento do produto
        binding.cadastroProdutoBtnSalvar.setOnClickListener {
            val handlerProdutoSave = setCoroutineExceptionHandler(
                errorMessage = "Erro ao salvar produto no banco de dados.",
                from = TAG,
            )

            lifecycleScope.launch(handlerProdutoSave) {
                usuarioHelper.usuario.value?.let { usuario ->
                    val produto = createProduto(usuario.id)

                    if (produto.isValid) {
                        produtosRepository.create(produto)

                        binding.cadastroProdutoBtnSalvar.isEnabled = false
                        finish()
                    }
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
            id = produtoToEdit?.id,
            titulo = titulo,
            descricao = descricao,
            valor = valorCasted,
            imagemUrl = imageUrl,
            usuarioId = usuarioId,
        )
    }
}
