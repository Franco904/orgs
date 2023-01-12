package com.example.orgs.extensions

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.navigateTo(component: Class<*>, intentCallback: Intent.() -> Unit = {}) {
    Intent(this, component).apply {
        intentCallback()
        startActivity(this)
    }
}

fun Context.showToast(errorMessage: String) {
    Toast.makeText(
        this,
        errorMessage,
        Toast.LENGTH_SHORT,
    ).show()
}
