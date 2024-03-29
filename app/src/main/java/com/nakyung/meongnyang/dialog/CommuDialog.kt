package com.nakyung.meongnyang.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.community.PostFragment

class CommuDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var cancelBtn: Button
    private lateinit var deleteBtn: Button

    fun delShow(postId: Int) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.commu_dialog)
        dialog.setCancelable(false)

        cancelBtn = dialog.findViewById(R.id.cancel_btn)
        deleteBtn = dialog.findViewById(R.id.delete_btn)

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        deleteBtn.setOnClickListener {
            dialog.dismiss()
            PostFragment().deletePost(postId)
        }

        dialog.show()
    }
}