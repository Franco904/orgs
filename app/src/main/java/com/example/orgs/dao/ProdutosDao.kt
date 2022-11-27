package com.example.orgs.dao

import com.example.orgs.model.Produto

class ProdutosDao {
    fun create(produto: Produto) {
        produtos.add(produto)
    }

    fun findAll() : List<Produto> {
        return produtos.toList()
    }

    companion object {
        private val produtos = mutableListOf<Produto>()
    }
}