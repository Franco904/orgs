package com.example.orgs.data.database.repositories

import ModelTestUtils.createUsuarioEntity
import com.example.orgs.contracts.data.database.repositories.UsuariosRepository
import com.example.orgs.data.database.dao.UsuariosDao
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@OptIn(ExperimentalCoroutinesApi::class)
class UsuariosRepositoryImplTest {
    private val usuariosDao = mockk<UsuariosDao>(relaxed = true)
    private val sut = UsuariosRepositoryImpl(dao = usuariosDao)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `Deve criar uma instancia de UsuariosRepository`() {
        sut.shouldBeInstanceOf<UsuariosRepository>()
    }

    @Nested
    @DisplayName("create")
    inner class CreateTest {
        @Test
        fun `Deve chamar o metodo create() do DAO quando executado`() = runTest {
            val usuario = createUsuarioEntity()

            sut.create(usuario)

            coVerify(exactly = 1) {
                usuariosDao.create(usuario)
            }
        }
    }

    @Nested
    @DisplayName("findByUserAndPassword")
    inner class FindByUserAndPasswordTest {
        @Test
        fun `Deve chamar o metodo findByUserAndPassword() do DAO quando executado`() = runTest {
            val usuario = createUsuarioEntity()

            sut.findByUserAndPassword(
                usuario = usuario.usuario,
                senha = usuario.senha,
            )

            coVerify(exactly = 1) {
                usuariosDao.findByUserAndPassword(
                    usuario = usuario.usuario,
                    senha = usuario.senha,
                )
            }
        }
    }

    @Nested
    @DisplayName("findByNameId")
    inner class FindByNameIdTest {
        @Test
        fun `Deve chamar o metodo findByNameId() do DAO quando executado`() {
            val usuario = createUsuarioEntity()

            sut.findByNameId(nameId = usuario.usuario)

            verify(exactly = 1) {
                usuariosDao.findByNameId(nameId = usuario.usuario)
            }
        }
    }

    @Nested
    @DisplayName("findAllWithProdutos")
    inner class FindAllWithProdutosTest {
        @Test
        fun `Deve chamar o metodo findAllWithProdutos() do DAO quando executado`() = runTest {
            sut.findAllWithProdutos()

            coVerify(exactly = 1) {
                usuariosDao.findAllWithProdutos()
            }
        }
    }
}