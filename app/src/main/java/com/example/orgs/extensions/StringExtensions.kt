package com.example.orgs.extensions

import java.security.MessageDigest

fun String.capitalize(): String {
    val capitalized = this.lowercase().also {
        it[0].uppercaseChar()
    }
    return capitalized
}

fun String.toHash(
    algorithm: String = "SHA-256"
): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(this.toByteArray())
        .fold("", { str, byte ->
            str + "%02x".format(byte)
        })
}

