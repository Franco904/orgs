package com.example.orgs.dao

import com.example.orgs.model.Produto
import java.math.BigDecimal

class ProdutosDao {
    fun create(produto: Produto) {
        produtos.add(produto)
    }

    fun findAll(): List<Produto> {
        return produtos.toList()
    }

    companion object {
        private val produtos = mutableListOf(
            Produto(
                "Maçãs",
                "Maçãs suculentas",
                BigDecimal("15.90"),
                "https://images-prod.healthline.com/hlcmsresource/images/AN_images/health-benefits-of-apples-1296x728-feature.jpg"
            ),
            Produto(
                "Blueberries",
                "Blueberries suculentas",
                BigDecimal("7.95"),
                "https://www.pensenatural.com.br/wp-content/uploads/2018/03/beneficios-blueberry.jpg"
            ),
            Produto(
                "Brócolis",
                "Brócolis suculentos",
                BigDecimal("5.00"),
                "https://s1.static.brasilescola.uol.com.br/be/conteudo/images/o-brocolis-pode-ser-preparado-no-vapor-5b4f5289ec913.jpg"
            ),
            Produto(
                "Laranjas",
                "Bastante popular no Brasil, a laranja é uma das maiores representantes das frutas cítricas. Seu sabor costuma ser doce ou levemente ácido (mais azedo), de acordo com o tipo da laranja. Além de ser rica em vitamina C, a fruta possui flavonoides e diversos nutrientes importantes para prevenir doenças e deixar o organismo mais resistente. A laranja faz parte do cardápio de diversas pessoas que optam pelo suco da fruta pela manhã ou para acompanhar refeições. Além de ser usada como ingrediente de diversas receitas ou consumida in natura. A fruta está disponível para o consumo praticamente durante todo o ano.",
                BigDecimal("12.80"),
                "https://static.historiadomundo.com.br/2019/07/cesta-com-laranjas.jpg"
            ),
        )
    }
}