package com.example.meongnyang.Dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.meongnyang.R
import kotlinx.coroutines.CoroutineScope

class ProgressDialog(context: Context) : Dialog(context) {

    init {
        setCanceledOnTouchOutside(false)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(R.layout.progress_dialog)
    }
}