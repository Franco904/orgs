package com.example.orgs.data.model

import com.example.orgs.data.model.Produto
import org.amshove.kluent.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ProdutoTest {
    private lateinit var sut: Produto

    @Test
    fun `Deve criar uma instancia de Produto com os atributos corretos quando inicializada`() {
        sut = Produto(
            titulo = "Alcátra bovina",
            descricao = "Uma carne macia para um bom almoço",
            valor = BigDecimal(37.99),
        )

        sut.shouldBeInstanceOf<Produto>()
        sut.id.shouldBeEqualTo(0L)
        sut.titulo.shouldBeEqualTo("Alcátra bovina")
        sut.descricao.shouldBeEqualTo("Uma carne macia para um bom almoço")
        sut.valor.shouldBeEqualTo(BigDecimal(37.99))
        sut.imagemUrl.shouldBeNull()
        sut.usuarioId.shouldBeEqualTo(0L)
    }

    @Test
    fun `Deve criar uma instancia valida de Produto para valores entre 1 e 100`() {
        sut = Produto(
            titulo = "Cesta de frutas",
            descricao = "Uma cesta de frutas para um lanche da tarde",
            valor = BigDecimal(17.99),
        )

        sut.isValid.shouldBeTrue()
    }

    @Test
    fun `Deve criar uma instancia invalida de Produto para valores iguais a 0`() {
        sut = Produto(
            titulo = "Cesta de frutas",
            descricao = "Uma cesta de frutas para um lanche da tarde",
            valor = BigDecimal(0.0),
        )

        sut.isValid.shouldBeFalse()
    }

    @Test
    fun `Deve criar uma instancia invalida de Produto para valores menores que 0`() {
        sut = Produto(
            titulo = "Cesta de frutas",
            descricao = "Uma cesta de frutas para um lanche da tarde",
            valor = BigDecimal(-9),
        )

        sut.isValid.shouldBeFalse()
    }

    @Test
    fun `Deve criar uma instancia invalida de Produto para valores maiores que 100`() {
        sut = Produto(
            titulo = "Cesta de frutas",
            descricao = "Uma cesta de frutas para um lanche da tarde",
            valor = BigDecimal(120.99),
        )

        sut.isValid.shouldBeFalse()
    }
}