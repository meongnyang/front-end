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
    ): Call<UserModel>
    @GET("members/{memberId}")
    fun getMembers(
        @Path("memberId") memberId: Int
    ): Call<UserModel>

    // 커뮤니티 모든 글 받아오기
    @GET("posts")
    fun findPosts(
    ): Call <List<GetPosts>>

    // 게시글 작성하기
    @POST("posts/{memberId}")
    fun createPost(
        @Body jsonparams: PostModel,
        @Path("memberId") memberId: Int
    ): Call<GetPosts>

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