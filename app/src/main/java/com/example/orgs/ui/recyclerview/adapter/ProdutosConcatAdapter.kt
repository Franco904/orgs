package com.example.orgs.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.data.model.Produto
import com.example.orgs.databinding.ProdutoItemBinding
import com.example.orgs.util.extensions.formatToRealCurrency
import com.example.orgs.util.extensions.tryLoadImage

class ProdutosConcatAdapter
    : ListAdapter<Produto, ProdutosConcatAdapter.ProdutosViewHolder>(DIFF_CALLBACK) {

    companion object {
        // Necessário para realizar a comparação entre os estados da mesma lista e assim
        // saber o que precisa mudar ou ser mantido igual (eleva a performance)
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Produto>() {
            // Compara por atributo único
            override fun areItemsTheSame(
                oldItem: Produto,
                newItem: Produto,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            // Compara todos os atributos do objeto, inclusive código hashMap de memória
            override fun areContentsTheSame(
                oldItem: Produto,
                newItem: Produto,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ProdutosViewHolder(
        val binding: ProdutoItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val card = binding.produtoCard

        fun bindProduto(produto: Produto) {
            binding.apply {
                produtoItemTitulo.text =
                    if (produto.titulo == "") "Sem nome definido" else produto.titulo
                produtoItemDescricao.text =
                    if (produto.descricao == "") "Sem descrição" else produto.descricao
                produtoItemValor.text = produto.valor.formatToRealCurrency()

                produtoItemImage.tryLoadImage(url = produto.imagemUrl)
            }
        }

        companion object {
            fun create(parentViewGroup: ViewGroup): ProdutosViewHolder {
                val binding = ProdutoItemBinding.inflate(
                    LayoutInflater.from(parentViewGroup.context),
                    parentViewGroup,
                    false,
                )

                return ProdutosViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProdutosViewHolder {
        return ProdutosViewHolder.create(parentViewGroup = parent)
    }

    override fun onBindViewHolder(holder: ProdutosViewHolder, position: Int) {
        val produto = getItem(position)

        holder.bindProduto(produto)
    }
}