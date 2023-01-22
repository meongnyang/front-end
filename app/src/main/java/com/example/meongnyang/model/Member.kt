package com.example.meongnyang.model

data class PostUser(
    val nickname: String,
    val email: String,
    val profile: String?
)

data class UserModel(
    var memberid: Int,
    var nickname: String
)
