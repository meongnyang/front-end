package com.example.meongnyang.model

data class BaseResponse<Data>(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data?
)
