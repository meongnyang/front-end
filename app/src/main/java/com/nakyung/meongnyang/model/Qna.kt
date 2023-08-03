package com.nakyung.meongnyang.model

data class Qna(
    val qnaId: Int,
    val question: String,
    val answer: String
)

data class QnaModel(
    val question: String,
    val answer: String
)
