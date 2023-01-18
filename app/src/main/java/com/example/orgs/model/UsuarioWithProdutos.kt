package com.example.orgs.model

import androidx.room.Embedded
import androidx.room.Relation

data class UsuarioWithProdutos(
    @Embedded val usuario: Usuario,
    @Relation(
        parentColumn = "id",
        entityColumn = "usuarioId",
    )
    val produtos: List<Produto>,
)