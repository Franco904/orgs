package com.example.orgs.ui.widget

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import com.example.orgs.R

class ProdutoCardPopupMenu(
    private val context: Context,
    private val cardView: View,
) {
    fun show(
        onEditDelegate: () -> Unit?,
        onExcludeDelegate: () -> Unit?,
    ) {
        PopupMenu(context, cardView).apply {
            inflate(R.menu.menu_detalhes_actions)

            setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        onEditDelegate()
                        true
                    }
                    R.id.action_exclude -> {
                        onExcludeDelegate()
                        true
                    }
                    else -> false
                }
            })

            show()
        }
    }
}