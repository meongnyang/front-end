package com.example.meongnyang.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.meongnyang.R

class FeedDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var nameTV: TextView
    private lateinit var materialTV: TextView
    private lateinit var ingredientTV: TextView
    private lateinit var closeBtn: Button


    fun show(name: String, material: String, ingredient: String) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.feed_fragment_dialog)
        dialog.setCancelable(false)

        nameTV = dialog.findViewById(R.id.name)
        materialTV = dialog.findViewById(R.id.material)
        ingredientTV = dialog.findViewById(R.id.ingredient)
        closeBtn = dialog.findViewById(R.id.closeBtn)

        nameTV.text = name
        materialTV.text = material
        ingredientTV.text = ingredient

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}