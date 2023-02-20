package com.example.orgs.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.orgs.constants.ID_DEFAULT

private const val EMAIL_REGEX = "^[A-Za-z](.*)([@])(.+)(\\.)(.{1,})"

@Entity
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = ID_DEFAULT,
    val usuario: String,
    val nome: String,
    val senha: String,
) {
    @Ignore
    private val isValidEmail = usuario.matches(Regex(EMAIL_REGEX))

    @Ignore
    private val isValidSenha = senha.count() >= 6

    @Ignore
    val isValid = isValidEmail && isValidSenha
}
