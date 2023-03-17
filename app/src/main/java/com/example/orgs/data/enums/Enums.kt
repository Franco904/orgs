package com.example.orgs.data.enums

enum class ProdutoField { TITULO, DESCRICAO, VALOR }

enum class OrderingPattern { ASC, DESC }

fun getProdutoFieldEnumByName(name: String): ProdutoField {
    return when (name) {
        "Título", "Nenhum" -> ProdutoField.TITULO
        "Descrição" -> ProdutoField.DESCRICAO
        "Valor" -> ProdutoField.VALOR
        else -> throw NotImplementedError()
    }
}

fun getOrderingPatternEnumByName(name: String): OrderingPattern {
    return when (name) {
        "Crescente" -> OrderingPattern.ASC
        "Decrescente" -> OrderingPattern.DESC
        else -> throw NotImplementedError()
    }
}
