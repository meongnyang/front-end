package com.nakyung.meongnyang

import android.widget.ImageView
import androidx.databinding.BindingAdapter

object WalkBindingAdapter {
    @BindingAdapter("imgRes")
    @JvmStatic fun loadImage(imageView: ImageView, resid: Int) {
        imageView.setImageResource(resid)
    }
}