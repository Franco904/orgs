package com.example.orgs.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.orgs.constants.ID_DEFAULT

@Entity
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = ID_DEFAULT,
    val usuario: String,
    val nome: String,
    val senha: String,
)
