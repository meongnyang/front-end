package com.nakyung.meongnyang.model

data class BaseResponse<Data>(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data?
)
