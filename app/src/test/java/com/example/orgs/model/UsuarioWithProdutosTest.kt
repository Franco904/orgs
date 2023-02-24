package com.example.orgs.model

import ModelTestUtils.createProdutoEntity
import ModelTestUtils.createUsuarioEntity
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class UsuarioWithProdutosTest {
    private lateinit var sut: UsuarioWithProdutos

    @Test
    fun `Deve criar uma instancia de UsuarioWithProdutos com os atributos corretos quando inicializada`() {
        val usuario = createUsuarioEntity()
        val produtos = mutableListOf(
            createProdutoEntity(),
            createProdutoEntity(),
            createProdutoEntity(),
        )

        sut = UsuarioWithProdutos(
            usuario = usuario,
            produtos = produtos,
        )

        sut.shouldBeInstanceOf<UsuarioWithProdutos>()

        sut.usuario.shouldBeEqualTo(usuario)
        sut.produtos.shouldBeEqualTo(produtos)
    }
}