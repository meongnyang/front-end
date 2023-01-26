package com.example.meongnyang.model

data class PostDiary(
    val conimalId: Int,
    val memberId: Int,
    val meal: Int,
    val voiding: Int,
    val voiding_reason: String,
    val excretion: Int,
    val excretion_reason: String
)

data class DiaryModel(
    val recordId: Int,
    val meal: Int,
    val voiding: Int,
    val voiding_reason: String,
    val excretion: Int,
    val excretion_reason: String,
    val date: String
)