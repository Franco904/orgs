import com.example.orgs.model.Produto
import com.example.orgs.model.Usuario
import com.example.orgs.model.UsuarioWithProdutos
import io.github.serpro69.kfaker.Faker
import java.math.BigDecimal

object ModelTestUtils {
    private val faker by lazy { Faker() }

    fun createProdutoEntity(
        id: Long? = null,
        titulo: String? = null,
        descricao: String? = null,
        valor: BigDecimal? = null,
        imagemUrl: String? = null,
        usuarioId: Long? = null,
    ): Produto {
        return Produto(
            id = id,
            titulo = titulo ?: faker.random.randomString(10),
            descricao = descricao ?: faker.random.randomString(10),
            valor = valor ?: BigDecimal(faker.random.nextDouble()),
            imagemUrl = imagemUrl,
            usuarioId = usuarioId,
        )
    }

    fun createUsuarioEntity(
        usuario: String? = null,
        nome: String? = null,
        senha: String? = null,
    ): Usuario {
        return Usuario(
            usuario = usuario ?: faker.random.randomString(15),
            nome = nome ?: faker.random.randomString(15),
            senha = senha ?: faker.random.randomString(15),
        )
    }

    fun createUsuarioWithProdutosEntity(
        usuario: Usuario? = null,
        produtos: List<Produto>? = null,
    ): UsuarioWithProdutos {
        return UsuarioWithProdutos(
            usuario = usuario ?: createUsuarioEntity(),
            produtos = produtos ?: mutableListOf(
                createProdutoEntity(),
            ),
        )
    }
}