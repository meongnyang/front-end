package com.nakyung.meongnyang.model

// 보내는 데이터
data class Walk(
    val latitude: Double,
    val longitude: Double,
    val category: String
)

// 받을 때 데이터 형식
data class Score(
    val index: String,
    val explanation: String,
    val temperature: Int,
    val o3: Double,
    val pm10: Int,
    val pm25: Int,
    val o3exp: String,
    val pm10exp: String,
    val pm25exp: String,
    val weather: String
)
