package com.nakyung.meongnyang

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object PostBindingAdapter {
    @BindingAdapter("postImgFromUrl")
    @JvmStatic fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .into(view)
    }
}