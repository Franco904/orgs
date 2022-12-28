package com.example.orgs.ui.recyclerview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
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
    var onProdutoItemSelected: (produto: Produto) -> Unit = {}

    inner class ViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val titulo = binding.produtoItemTitulo
        val descricao = binding.produtoItemDescricao
        val valor = binding.produtoItemValor
        val imagem = binding.produtoItemImage

        val card = binding.produtoCard

        fun bindProduto(produto: Produto) {
            titulo.text = if (produto.titulo == "") "Sem nome definido" else produto.titulo
            descricao.text = if (produto.descricao == "") "Sem descrição" else produto.descricao
            valor.text = produto.valor.formatToRealCurrency()

            imagem.tryLoadImage(produto.imagemUrl)
        }

        fun setUpProdutoSelection(produto: Produto) {
            card.setOnClickListener {
                onProdutoItemSelected(produto)
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
        holder.setUpProdutoSelection(produto)
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
