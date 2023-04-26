package com.example.meongnyang.model

data class Walk(
    val nx: String,
    val ny: String,
    val city: String,
    val district: String
)

data class Score(
    val index: String,
    val explanation: String,
    val temperature: Int,
    val o3: Double,
    val pm10: Int,
    val pm25: Int
)
