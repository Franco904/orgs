package com.example.orgs.model

import androidx.room.*
import com.example.orgs.constants.ID_DEFAULT
import java.math.BigDecimal

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("usuarioId"),
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["usuarioId"])
    ],
)
data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = ID_DEFAULT,
    val titulo: String,
    val descricao: String,
    val valor: BigDecimal,
    val imagemUrl: String? = null,
    val usuarioId: Long? = ID_DEFAULT,
) {
    @Ignore
    val isValid = valorGreaterThanZero() && valorLessThanOrEqualToOneHundred()

    private fun valorGreaterThanZero(): Boolean {
        return valor > BigDecimal.ZERO
    }

    private fun valorLessThanOrEqualToOneHundred(): Boolean {
        return valor <= BigDecimal(100)
    }
}
