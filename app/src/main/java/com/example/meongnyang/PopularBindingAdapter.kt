package com.example.meongnyang

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

object PopularBindingAdapter {
    @BindingAdapter("popularImgFromUrl")
    @JvmStatic fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_mypage)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}