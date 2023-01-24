package com.example.meongnyang.model

data class PostDiary(
    val conimalId: Int,
    val memberId: Int,
    val meal: Int,
    val voiding: Int,
    val voidReason: String,
    val excretion: Int,
    val excReason: String
)

data class DiaryModel(
    val recordId: Int,
    val meal: String,
    val voiding: String,
    val excretion: String
)

data class ShowDiary(
    val conimalId: Int,
    val meal: String,
    val voiding: String,
    val excretion: String,
    val date: String
)