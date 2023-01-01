package com.example.orgs.extensions

fun String.capitalize(): String {
    val capitalized = this.lowercase().also {
        it[0].uppercaseChar()
    }
    return capitalized
}
