package com.example.orgs.dao

import com.example.orgs.model.Produto
import java.math.BigDecimal

class ProdutosDao {
    fun create(produto: Produto) {
        produtos.add(produto)
    }

    fun findAll(): List<Produto> {
        return produtos.toList()
    }

    companion object {
        private val produtos = mutableListOf(
            Produto(
                "Maçãs",
                "Maçãs suculentas",
                BigDecimal("15.90"),
                "https://images-prod.healthline.com/hlcmsresource/images/AN_images/health-benefits-of-apples-1296x728-feature.jpg"
            ),
        )
    }
}