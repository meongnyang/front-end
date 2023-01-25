package com.example.meongnyang.api

import com.example.meongnyang.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitApi {
    // 회원 API
    @POST("members")
    fun userSignUp(
        @Body postUser: PostUser
    ): Call<UserModel>
    @GET("mypage/{memberId}")
    fun getMember(
        @Path("memberId") memberId: Int
    ): Call<User>
    @PATCH("members/update")
    fun userUpdate(
        @Body img: String
    ): Call<User>

    // 건강기록부 API
    @POST("records/{memberId}/{conimalId}")
    fun writeDiary(
        @Body diary: PostDiary
    ): Call<DiaryModel>
    @PATCH("records/update/{memberId}/{conimalId}")
    fun updateDiary(
        @Path ("memberId", encoded = true) memberId: Int,
        @Path ("conimalId", encoded = true) conimalId: Int,
        @Body diary: PostDiary
    ): Call<DiaryModel>
    @GET("records/{memberId}/{conimalId}")
    fun showDiary(
        @Path("memberId") memberId: Int,
        @Path("conimalId") conimalId: Long
    ): Call<ShowDiary>

    // 반려동물 API
    @POST("conimals/{memberId}")
    fun enrollPet(
        @Path ("memberId", encoded = true) memberId: Int,
        @Body pet: Pet
    ): Call<PetModel>
    @GET("conimals/{conimalId}")
    fun getPet(
        @Path("conimalId") conimalId: Int
    ): Call<PetModel>

    // 커뮤니티 API
    @GET("posts")
    fun findPosts(
    ): Call <List<GetPosts>>

    // 게시글 작성하기
    @POST("posts/{memberId}")
    fun createPost(
        @Body jsonparams: PostModel,
        @Path("memberId") memberId: Int
    ): Call<GetPosts>
    // 게시글 자세히 보기
    @GET("posts/{postId}")
    fun getPost(
        @Path("postId") postId: Int
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