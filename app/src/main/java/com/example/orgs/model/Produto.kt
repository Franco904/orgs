package com.example.orgs.model

import java.math.BigDecimal

data class Produto(
    val titulo: String,
    val descricao: String,
    val valor: BigDecimal,
    val imagemUrl: String? = null,
)
