package com.example.meongnyang.model

data class Post(
    val title: String,
    val categoryName: String
)

data class GetPosts(
    val postId: Int,
    val category: Int,
    val categoryName: String,
    val type: Int,
    val title: String,
    val contents: String,
    val count: Int,
    val img: String,
    val memberId: Int,
    val date: String,
    val time: String
)

data class PostModel(
    val category: Int,
    val type: Int,
    val title: String,
    val contents: String,
    val img: String
)
