package com.example.meongnyang.api

import com.example.meongnyang.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitApi {
    // 회원 관련
    @POST("members")
    fun postMembers(
        @Body postUser: PostUser
    ): Call<PostResult>

    @GET("members/{memberid}")
    fun getMembers(
        @Path("memberid") memberid: Int,
    ): Call<UserModel>

    // 커뮤니티 모든 글 받아오기
    @GET("posts")
    fun getAllPosts(
    ): Call <List<GetPosts>>

    companion object {
        private const val BASE_URL = "http://43.201.122.215:8080/"

        fun create(): RetrofitApi {
            val gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitApi::class.java)
        }
    }

}