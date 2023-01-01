package com.example.orgs.enums

enum class ProdutoField { TITULO, DESCRICAO, VALOR, NENHUM }

enum class OrderingPattern { ASC, DESC }

fun getProdutoFieldEnumByName(name: String): ProdutoField {
    return when (name) {
        "Título" -> ProdutoField.TITULO
        "Descrição" -> ProdutoField.DESCRICAO
        "Valor" -> ProdutoField.VALOR
        else -> ProdutoField.NENHUM
    }
}

fun getOrderingPatternEnumByName(name: String): OrderingPattern {
    return when (name) {
        "Crescente" -> OrderingPattern.ASC
        "Decrescente" -> OrderingPattern.DESC
        else -> throw NotImplementedError()
    }
}
