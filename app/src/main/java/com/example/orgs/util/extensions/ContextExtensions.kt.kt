package com.example.orgs.util.extensions

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineExceptionHandler

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

fun Context.setCoroutineExceptionHandler(
    errorMessage: String,
    from: String,
    customExceptionCallback: () -> Unit = {},
): CoroutineExceptionHandler {
    return CoroutineExceptionHandler { _, throwable ->
        Log.i(from, "CoroutineScope error: $throwable")
        showToast(errorMessage)

        customExceptionCallback()
    }
}
