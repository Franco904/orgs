package com.example.orgs.database.repositories

import ModelTestUtils.createUsuarioEntity
import ModelTestUtils.createUsuarioWithProdutosEntity
import com.example.orgs.contracts.data.database.repositories.UsuariosRepository
import com.example.orgs.data.database.dao.UsuariosDao
import com.example.orgs.data.database.repositories.UsuariosRepositoryImpl
import com.example.orgs.data.model.Usuario
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@OptIn(ExperimentalCoroutinesApi::class)
class UsuariosRepositoryImplTest {
    private val usuariosDao = mockk<UsuariosDao>()
    private val sut = UsuariosRepositoryImpl(dao = usuariosDao)

    private fun mockCreate(usuario: Usuario) {
        coEvery {
            usuariosDao.create(usuario)
        }.returns(Unit)
    }

    private fun mockFindByUserAndPassword(usuario: Usuario) {
        coEvery {
            usuariosDao.findByUserAndPassword(
                usuario = usuario.usuario,
                senha = usuario.senha,
            )
        }.returns(usuario)
    }

    private fun mockFindByNameId(usuario: Usuario) {
        every {
            usuariosDao.findByNameId(nameId = usuario.usuario)
        }.returns(flow {
            emit(usuario)
        })
    }

    private fun mockFindAllWithProdutos() {
        coEvery {
            usuariosDao.findAllWithProdutos()
        }.returns(
            mutableListOf(
                createUsuarioWithProdutosEntity(),
                createUsuarioWithProdutosEntity(),
                createUsuarioWithProdutosEntity(),
            )
        )
    }

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
            mockCreate(usuario)

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
            mockFindByUserAndPassword(usuario)

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
            mockFindByNameId(usuario)

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
            mockFindAllWithProdutos()

            sut.findAllWithProdutos()

            coVerify(exactly = 1) {
                usuariosDao.findAllWithProdutos()
            }
        }
    }
}