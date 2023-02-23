package com.example.orgs.model

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test

class UsuarioTest {
    private lateinit var sut: Usuario

    @Test
    fun `Deve criar uma instancia de Usuario com os atributos corretos quando inicializada`() {
        sut = Usuario(
            usuario = "francostavares2003@gmail.com",
            nome = "Franco Saravia Tavares",
            senha = "123123as",
        )

        sut.shouldBeInstanceOf<Usuario>()
        sut.id.shouldBeEqualTo(0L)
        sut.usuario.shouldBeEqualTo("francostavares2003@gmail.com")
        sut.nome.shouldBeEqualTo("Franco Saravia Tavares")
        sut.senha.shouldBeEqualTo("123123as")
    }

    @Test
    fun `Deve criar uma instancia valida de Usuario para valores de email e senha validos`() {
        sut = Usuario(
            usuario = "francostavares2003@gmail.com",
            nome = "Franco Saravia Tavares",
            senha = "123123as",
        )

        sut.isValid.shouldBeTrue()
    }

    @Test
    fun `Deve criar uma instancia invalida de Usuario para valores de email invalidos`() {
        sut = Usuario(
            usuario = "francogmail.com",
            nome = "Franco Saravia Tavares",
            senha = "123123as",
        )

        sut.isValid.shouldBeFalse()
    }

    @Test
    fun `Deve criar uma instancia invalida de Usuario para valores de senha inferiores a 6 caracteres`() {
        sut = Usuario(
            usuario = "francostavares2003@gmail.com",
            nome = "Franco Saravia Tavares",
            senha = "one",
        )

        sut.isValid.shouldBeFalse()
    }
}