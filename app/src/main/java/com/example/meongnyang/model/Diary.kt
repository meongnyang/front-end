package com.example.meongnyang.model

data class Diary(
    val conimalId: Int,
    val memberId: Int,
    val meal: Int,
    val voiding: Int,
    val voidReason: String,
    val excretion: Int,
    val excReason: String
)
