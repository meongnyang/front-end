package com.example.meongnyang.api


import com.example.meongnyang.model.ResultSearchKeyword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoAPI {
    @GET("v2/local/search/keyword.json")
    fun getSearchLocationDetail (
        @Header("Authorization") key: String, // 카카오 api 인증키 [필수]
        @Query("query") query: String, // 검색을 원하는 질의어 [필수]
        @Query("x") x: String, // 위도
        @Query("y") y: String, // 경도
        @Query("size") size: Int, // 범위
    ): Call<ResultSearchKeyword> // 받아온 정보가 ResultSearchKeyword 클래스의 구조로 담김
}