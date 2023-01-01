package com.example.orgs.ui.widget

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.orgs.databinding.ProdutoFormDialogBinding
import com.example.orgs.extensions.tryLoadImage

class CadastroProdutoImageDialog(private val context: Context) {
    fun show(
        currentImageUrl: String?,
        onImageLoadedDelegate: (String?) -> Unit,
    ) {
        ProdutoFormDialogBinding.inflate(LayoutInflater.from(context)).apply {
            // Carrega imagem atual ao abrir dialog
            currentImageUrl?.let {
                updateImageItemImage.tryLoadImage(url = it)
                updateImageFieldUrl.editText?.setText(it)
            }

            updateImageBtnCarregar.setOnClickListener {
                val url = updateImageFieldUrl.editText?.text.toString()
                // Carrega imagem da URL dentro da dialog
                updateImageItemImage.tryLoadImage(url)
            }

            AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton("Confirmar") { _, _ ->
                    val newUrl = updateImageFieldUrl.editText?.text.toString()
                    onImageLoadedDelegate(newUrl)
                }
                .setNegativeButton("Cancelar") { _, _ -> }
                .show()
        }
    }
}