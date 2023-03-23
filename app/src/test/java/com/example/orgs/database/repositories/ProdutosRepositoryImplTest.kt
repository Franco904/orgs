package com.example.orgs.database.repositories

import ModelTestUtils.createProdutoEntity
import com.example.orgs.contracts.data.database.repositories.ProdutosRepository
import com.example.orgs.data.database.dao.ProdutosDao
import com.example.orgs.data.database.repositories.ProdutosRepositoryImpl
import com.example.orgs.data.enums.OrderingPattern
import com.example.orgs.data.enums.ProdutoField
import com.example.orgs.data.model.Produto
import io.github.serpro69.kfaker.Faker
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProdutosRepositoryImplTest {
    private val produtosDao = mockk<ProdutosDao>()
    private val sut = ProdutosRepositoryImpl(dao = produtosDao)

    private val faker by lazy { Faker() }

    private fun mockCreate(produto: Produto) {
        coEvery {
            produtosDao.create(produto)
        }.returns(Unit)
    }

    private fun mockDelete(produto: Produto) {
        coEvery {
            produtosDao.delete(produto)
        }.returns(Unit)
    }

    private fun mockFindById(produtoId: Long) {
        val produto = createProdutoEntity(id = produtoId)

        coEvery {
            produtosDao.findById(produtoId)
        }.returns(produto)
    }

    private fun mockFindAllByUsuarioId(usuarioId: Long) {
        every {
            produtosDao.findAllByUsuarioId(usuarioId)
        }.returns(flow {
            emit(
                mutableListOf(
                    createProdutoEntity(),
                    createProdutoEntity(),
                    createProdutoEntity(),
                )
            )
        })
    }

    private fun mockFindAllOrderedByTituloAsc(usuarioId: Long) {
        coEvery {
            produtosDao.findAllOrderedByTituloAsc(usuarioId)
        }.returns(mutableListOf())
    }

    private fun mockFindAllOrderedByTituloDesc(usuarioId: Long) {
        coEvery {
            produtosDao.findAllOrderedByTituloDesc(usuarioId)
        }.returns(mutableListOf())
    }

    private fun mockFindAllOrderedByDescricaoAsc(usuarioId: Long) {
        coEvery {
            produtosDao.findAllOrderedByDescricaoAsc(usuarioId)
        }.returns(mutableListOf())
    }

    private fun mockFindAllOrderedByDescricaoDesc(usuarioId: Long) {
        coEvery {
            produtosDao.findAllOrderedByDescricaoDesc(usuarioId)
        }.returns(mutableListOf())
    }

    private fun mockFindAllOrderedByValorAsc(usuarioId: Long) {
        coEvery {
            produtosDao.findAllOrderedByValorAsc(usuarioId)
        }.returns(mutableListOf())
    }

    private fun mockFindAllOrderedByValorDesc(usuarioId: Long) {
        coEvery {
            produtosDao.findAllOrderedByValorDesc(usuarioId)
        }.returns(mutableListOf())
    }

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `Deve criar uma instancia de ProdutosRepository`() {
        sut.shouldBeInstanceOf<ProdutosRepository>()
    }

    @Nested
    @DisplayName("create")
    inner class CreateTest {
        @Test
        fun `Deve chamar o metodo create() do DAO quando executado`() = runTest {
            val produto = createProdutoEntity()
            mockCreate(produto)

            sut.create(produto)

            coVerify(exactly = 1) {
                produtosDao.create(produto)
            }
        }
    }

    @Nested
    @DisplayName("delete")
    inner class DeleteTest {
        @Test
        fun `Deve chamar o metodo delete() do DAO quando executado`() = runTest {
            val produto = createProdutoEntity()
            mockDelete(produto)

            sut.delete(produto)

            coVerify(exactly = 1) {
                produtosDao.delete(produto)
            }
        }
    }

    @Nested
    @DisplayName("findById")
    inner class FindByIdTest {
        @Test
        fun `Deve chamar o metodo findById() do DAO quando executado`() = runTest {
            val id = faker.random.nextLong(10L)
            mockFindById(id)

            sut.findById(id)

            coVerify(exactly = 1) {
                produtosDao.findById(id)
            }
        }
    }

    @Nested
    @DisplayName("findAllByUsuarioId")
    inner class FindAllByUsuarioId {
        @Test
        fun `Deve chamar o metodo findAllByUsuarioId() do DAO quando executado`() {
            val id = faker.random.nextLong(10L)
            mockFindAllByUsuarioId(id)

            sut.findAllByUsuarioId(id)

            coVerify(exactly = 1) {
                produtosDao.findAllByUsuarioId(id)
            }
        }
    }

    @Nested
    @DisplayName("findAllOrderedByField")
    inner class FindAllOrderedByField {
        @Test
        fun `Deve chamar o metodo findAllOrderedByTituloAsc() do DAO quando o campo filtrado for titulo e a ordenacao for crescente`() =
            runTest {
                val usuarioId = faker.random.nextLong(10L)
                mockFindAllOrderedByTituloAsc(usuarioId)

                sut.findAllOrderedByField(
                    field = ProdutoField.TITULO,
                    orderingPattern = OrderingPattern.ASC,
                    usuarioId = usuarioId,
                )

                coVerify(exactly = 1) {
                    produtosDao.findAllOrderedByTituloAsc(usuarioId)
                }
            }

        @Test
        fun `Deve chamar o metodo findAllOrderedByTituloDesc() do DAO quando o campo filtrado for titulo e a ordenacao for decrescente`() =
            runTest {
                val usuarioId = faker.random.nextLong(10L)
                mockFindAllOrderedByTituloDesc(usuarioId)

                sut.findAllOrderedByField(
                    field = ProdutoField.TITULO,
                    orderingPattern = OrderingPattern.DESC,
                    usuarioId = usuarioId,
                )

                coVerify(exactly = 1) {
                    produtosDao.findAllOrderedByTituloDesc(usuarioId)
                }
            }

        @Test
        fun `Deve chamar o metodo findAllOrderedByDescricaoAsc() do DAO quando o campo filtrado for descricao e a ordenacao for crescente`() =
            runTest {
                val usuarioId = faker.random.nextLong(10L)
                mockFindAllOrderedByDescricaoAsc(usuarioId)

                sut.findAllOrderedByField(
                    field = ProdutoField.DESCRICAO,
                    orderingPattern = OrderingPattern.ASC,
                    usuarioId = usuarioId,
                )

                coVerify(exactly = 1) {
                    produtosDao.findAllOrderedByDescricaoAsc(usuarioId)
                }
            }

        @Test
        fun `Deve chamar o metodo findAllOrderedByDescricaoDesc() do DAO quando campo filtrado for descricao e a ordenacao for decrescente`() =
            runTest {
                val usuarioId = faker.random.nextLong(10L)
                mockFindAllOrderedByDescricaoDesc(usuarioId)

                sut.findAllOrderedByField(
                    field = ProdutoField.DESCRICAO,
                    orderingPattern = OrderingPattern.DESC,
                    usuarioId = usuarioId,
                )

                coVerify(exactly = 1) {
                    produtosDao.findAllOrderedByDescricaoDesc(usuarioId)
                }
            }

        @Test
        fun `Deve chamar o metodo findAllOrderedByValorAsc() do DAO quando o campo filtrado for valor e a ordenacao for crescente`() =
            runTest {
                val usuarioId = faker.random.nextLong(10L)
                mockFindAllOrderedByValorAsc(usuarioId)

                sut.findAllOrderedByField(
                    field = ProdutoField.VALOR,
                    orderingPattern = OrderingPattern.ASC,
                    usuarioId = usuarioId,
                )

                coVerify(exactly = 1) {
                    produtosDao.findAllOrderedByValorAsc(usuarioId)
                }
            }

        @Test
        fun `Deve chamar o metodo findAllOrderedByValorDesc() do DAO quando o campo filtrado for valor e a ordenacao for decrescente`() =
            runTest {
                val usuarioId = faker.random.nextLong(10L)
                mockFindAllOrderedByValorDesc(usuarioId)

                sut.findAllOrderedByField(
                    field = ProdutoField.VALOR,
                    orderingPattern = OrderingPattern.DESC,
                    usuarioId = usuarioId,
                )

                coVerify(exactly = 1) {
                    produtosDao.findAllOrderedByValorDesc(usuarioId)
                }
            }
    }
}