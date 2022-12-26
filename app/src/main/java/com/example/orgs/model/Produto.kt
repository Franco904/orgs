package com.example.orgs.model

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal

data class Produto(
    val titulo: String,
    val descricao: String,
    val valor: BigDecimal,
    val imagemUrl: String? = null,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        titulo = parcel.readString() ?: "",
        descricao = parcel.readString() ?: "",
        valor = (parcel.readFloat()).toBigDecimal(),
        imagemUrl = parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(titulo)
        parcel.writeString(descricao)
        parcel.writeFloat(valor.toFloat())
        parcel.writeString(imagemUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Produto> {
        override fun createFromParcel(parcel: Parcel): Produto {
            return Produto(parcel)
        }

        override fun newArray(size: Int): Array<Produto?> {
            return arrayOfNulls(size)
        }
    }
}
