package com.nakyung.meongnyang

import android.widget.ImageView
import com.bumptech.glide.Glide
import androidx.databinding.BindingAdapter

object BindingAdapter {
    @BindingAdapter("imageFromUrl")
    @JvmStatic fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .circleCrop()
            .into(view)
    }
}