package com.example.orgs.ui.modules.detalhes_produto

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.contracts.ui.modules.detalhes_produto.DetalhesProdutoActivity
import com.example.orgs.util.constants.PRODUTO_ID_EXTRA
import com.example.orgs.databinding.ActivityDetalhesProdutoBinding
import com.example.orgs.util.extensions.tryLoadImage
import com.example.orgs.data.model.Produto
import com.example.orgs.ui.modules.cadastro_produto.CadastroProdutoActivityImpl
import com.example.orgs.ui.widget.ExcluirProdutoConfirmacaoDialog
import com.example.orgs.util.extensions.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class DetalhesProdutoActivityImpl : AppCompatActivity(), DetalhesProdutoActivity {
    private val viewModel: DetalhesProdutoViewModelImpl by viewModels()

    private val binding: ActivityDetalhesProdutoBinding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }

    private var produto: Produto? = null

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        supportActionBar?.hide()
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.produto.filterNotNull().collect {
                produto = it

                bindProdutoDataIfExist()
                setUpEditButtonListener()
                setUpDeleteButtonListener()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.tryFindProdutoInDatabase()
    }

    override fun bindProdutoDataIfExist() {
        binding.apply {
            produtoImage.tryLoadImage(url = produto?.imagemUrl)

            val currencyFormatter: NumberFormat =
                NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            produtoValor.text = currencyFormatter.format(produto?.valor)

            produtoTitulo.text = produto?.titulo?.ifEmpty { "Sem nome definido" }
            produtoDescricao.text = produto?.descricao?.ifEmpty { "Sem descrição" }
        }
    }

    override fun setUpEditButtonListener() {
        binding.editActionCard.setOnClickListener {
            navigateTo(CadastroProdutoActivityImpl::class.java) {
                putExtra(PRODUTO_ID_EXTRA, produto?.id)
            }
        }
    }

    override fun setUpDeleteButtonListener() {
        binding.excludeActionCard.setOnClickListener {
            ExcluirProdutoConfirmacaoDialog(context = this).show {
                viewModel.deleteProduto()
                finish()

                null
            }
        }
    }
}