package com.example.orgs.ui.widget

import android.content.Context
import androidx.appcompat.app.AlertDialog

class ExcluirProdutoConfirmacaoDialog(private val context: Context) {
    fun show(onExcludeDelegate: () -> Unit?) {
        AlertDialog.Builder(context)
            .setTitle("Deseja excluir o produto?")
            .setMessage("Uma vez efetuada a ação, o produto será excluído permanentemente.")
            .setPositiveButton("Confirmar") { _, _ -> onExcludeDelegate() }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
}