package com.example.orgs.ui.recyclerview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.databinding.ProdutoItemBinding
import com.example.orgs.extensions.formatToRealCurrency
import com.example.orgs.extensions.tryLoadImage
import com.example.orgs.model.Produto

class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto> = emptyList(),
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val produtos = produtos.toMutableList()
    var onProdutoItemClick: (produto: Produto) -> Unit = {}
    var onProdutoItemLongClick: (view: View, produto: Produto) -> Unit = { _: View, _: Produto -> }

    inner class ViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val titulo = binding.produtoItemTitulo
        private val descricao = binding.produtoItemDescricao
        private val valor = binding.produtoItemValor
        private val imagem = binding.produtoItemImage

        private val card = binding.produtoCard

        fun bindProduto(produto: Produto) {
            titulo.text = if (produto.titulo == "") "Sem nome definido" else produto.titulo
            descricao.text = if (produto.descricao == "") "Sem descrição" else produto.descricao
            valor.text = produto.valor.formatToRealCurrency()

            imagem.tryLoadImage(url = produto.imagemUrl)
        }

        fun setUpProdutoOnClick(produto: Produto) {
            card.setOnClickListener {
                onProdutoItemClick(produto)
            }
        }

        fun setUpProdutoOnLongClick(produto: Produto) {
            card.setOnLongClickListener {
                onProdutoItemLongClick(card, produto)

                // return true: Consome evento e não aciona onClick/outros listeners
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Padrão de inicialização do binding no adapter
        val binding = ProdutoItemBinding.inflate(
            LayoutInflater.from(context), parent, false
        )

        // Cria ViewHolder para o item de produto
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]

        // Com o layout ViewHolder criado, configuramos as ações de cada item de produto
        holder.bindProduto(produto)

        holder.setUpProdutoOnClick(produto)
        holder.setUpProdutoOnLongClick(produto)
    }

    override fun getItemCount(): Int = produtos.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapterState(produtos: List<Produto>) {
        this.produtos.clear()
        this.produtos.addAll(produtos)

        // Adapter vai reexecutar os métodos sobrescritos do ciclo de vida quando a lista de produtos for alterada
        notifyDataSetChanged()
    }
}
