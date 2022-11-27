package com.example.orgs.ui.recyclerview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.R
import com.example.orgs.databinding.ProdutoItemBinding
import com.example.orgs.model.Produto

class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto>,
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val produtos = produtos.toMutableList()

    class ViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val titulo = binding.produtoItemTitulo
        val descricao = binding.produtoItemDescricao
        val valor = binding.produtoItemValor

        fun bindProduto(produto: Produto) {
            titulo.text = produto.titulo
            descricao.text = produto.descricao
            valor.text = produto.valor.toPlainString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Padrão de inicialização do binding no adapter
        val binding = ProdutoItemBinding.inflate(
            LayoutInflater.from(context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]

        holder.bindProduto(produto)
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
