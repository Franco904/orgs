package com.example.orgs.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.databinding.UsuarioNameItemBinding
import com.example.orgs.data.model.Usuario

class UsuarioConcatAdapter
    : ListAdapter<Usuario, UsuarioConcatAdapter.UsuariosViewHolder>(DIFF_CALBACK) {

    companion object {
        private val DIFF_CALBACK = object : DiffUtil.ItemCallback<Usuario>() {
            override fun areItemsTheSame(
                oldItem: Usuario,
                newItem: Usuario,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Usuario,
                newItem: Usuario,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class UsuariosViewHolder(
        val binding: UsuarioNameItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindUsuarioName(usuarioName: String) {
            binding.usuarioItemName.text = usuarioName
        }

        companion object {
            fun create(parentViewGroup: ViewGroup): UsuariosViewHolder {
                val binding = UsuarioNameItemBinding.inflate(
                    LayoutInflater.from(parentViewGroup.context),
                    parentViewGroup,
                    false,
                )

                return UsuariosViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): UsuariosViewHolder {
        return UsuariosViewHolder.create(parentViewGroup = parent)
    }

    override fun onBindViewHolder(
        holder: UsuariosViewHolder,
        position: Int,
    ) {
        val usuario = getItem(position)

        holder.bindUsuarioName(usuario.nome)
    }
}