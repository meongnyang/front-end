package com.example.meongnyang

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import androidx.databinding.BindingAdapter
import com.example.meongnyang.R

object BindingAdapter {
    @BindingAdapter("imageFromUrl")
    @JvmStatic fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .circleCrop()
            .into(view)
    }
}