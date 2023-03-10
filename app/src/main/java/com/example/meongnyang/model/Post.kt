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
    val time: String,
    val nickname: String
)

data class PostModel(
    val category: Int,
    val type: Int,
    val title: String,
    val contents: String,
    val img: String
)

data class Comment(
    val commentId: Int,
    val contents: String,
    val memberId: Int,
    val parentId: Int,
    val postId: Int,
    val nickname: String,
    val commentList: ArrayList<reCommentModel>,
    val reComment: Boolean
)

data class CommentModel(
    val nickname: String,
    val contents: String
)

data class reCommentModel(
    val id: Int,
    val nickname: String,
    val contents: String,
    val reComment: Boolean
)

data class AllComment(
    val nickname: String,
    val contents: String,
    val commentList: ArrayList<reCommentModel>
)

data class Count (
    val count: Int
)

data class Title (
    val title: String
)

data class PostId (
    val postId: Int
    )

data class CommentId (
    val commentId: Int
        )

data class Contents (
    val contents: String
        )