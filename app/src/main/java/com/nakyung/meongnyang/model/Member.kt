package com.nakyung.meongnyang.model

data class PostUser(
    val nickname: String,
    val email: String,
    val password: String
)

data class LoginUser(
    val token: String,
    val memberId: Int,
    val email: String,
    val nickname: String
)

data class UserModel(
    val memberId: Int,
    val email: String,
    val nickname: String,
    val password: String
)

data class User(
    val nickname: String,
    val memberimg: String,
    val birth: String,
    val adopt: String,
    val conimalimg: String,
    val name: String,
    val gender: String,
    val species: String
)

data class Id(
    val memberId: Int? = null,
    val conimalId: Int? = null,
    val recordId: Int? = null
)

data class allPet(
    val nickname: String,
    val memberImg: String,
    val memberId: Int,
    val conimals: List<myPet>
)

data class Nickname(
    val nickname: String
)

data class Img(
    val img: String
)

data class Email(
    val email: String
)

data class MemberId(
    val memberId: Int
)
