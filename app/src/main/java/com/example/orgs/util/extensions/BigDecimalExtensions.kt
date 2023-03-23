package com.example.orgs.util.extensions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

fun BigDecimal.formatToRealCurrency(): String {
    val currencyFormatter: NumberFormat =
        NumberFormat.getCurrencyInstance(Locale("pt", "br"))

    return currencyFormatter.format(this)
}