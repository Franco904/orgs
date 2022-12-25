package com.example.orgs.extensions

import android.widget.ImageView
import coil.load
import com.example.orgs.R

fun ImageView.tryLoadImage(url: String?) {
    load(url) {
        fallback(R.drawable.imagem_padrao)
        error(R.drawable.imagem_padrao)
        placeholder(R.drawable.loading_indicator)
    }
}