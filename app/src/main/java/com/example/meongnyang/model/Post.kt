package com.example.meongnyang.model

data class Post(
    val memberId: Int,
    val postId: Int,
    val category: Int,
    val title: String,
    val contents: String,
    val date: String,
    val type: Int,
    val img: String
)
