package com.example.meongnyang.api

import com.example.meongnyang.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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
    ): Call<allPet>

    @FormUrlEncoded
    @PATCH("members/update")
    fun memberUpdate(
        @Field("nickname") nickname: String
    ): Call<Update>

    // 건강기록부 API
    @POST("records/{memberId}/{conimalId}")
    fun writeDiary(
        @Path ("memberId") memberId: Int,
        @Path ("conimalId") conimalId: Int,
        @Body diary: PostDiary
    ): Call<DiaryModel>

    @FormUrlEncoded
    @PATCH("records/update/{memberId}/{conimalId}")
    fun updateDiary(
        @Path ("memberId", encoded = true) memberId: Int,
        @Path ("conimalId", encoded = true) conimalId: Int,
        @Body diary: PostDiary
    ): Call<DiaryModel>

    @GET("records/{recordId}")
    fun showDiary(
        @Path("recordId") recordId: Int,
    ): Call<DiaryModel>

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

    // postId 반환
    @POST("posts/findid")
    fun findPostId(
        @Body title: Title
    ): Call<PostId>

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
    // 댓글 보기
    @GET("comments/{postId}")
    fun getComments(
        @Path("postId") postId: Int
    ): Call<List<Comment>>

    // 댓글 작성하기
    @POST("comments/{memberId}/{postId}")
    fun writeComment(
        @Path("memberId") memberId: Int,
        @Path("postId") postId: Int,
        @Body comments: String
    ): Call<Comment>

    // 좋아요 기능
    @POST("likes/{memberId}/{postId}")
    fun updateLikes(
        @Path("memberId") memberId: Int,
        @Path("postId") postId: Int
    ): Call<Count>

    // 피부병 관련
    @POST("disease")
    fun getDisease(
        @Body name: Name
    ): Call<Result>

    // 사료
    @GET("feed/type/{typeId}")
    fun getFeed(
        @Path("typeId") typeId: Int
    ): Call<List<Feed>>

    @GET("feed/{feedId}")
    fun getByFeedId(
        @Path("feedId") feedId: Int
    ): Call<Feed>

    @GET("feed/efficacy/{efficacyId}")
    fun getByEfficacy(
        @Path("efficacyId") efficacyId: Int
    ): Call<List<Feed>>

    // 수의사 질의응답
    @GET("qna")
    fun getAllQna(
    ): Call<List<Qna>>

    @GET("qna/{qnaId}")
    fun getQna(
        @Path("qnaId") qnaId: Int
    ): Call<QnaModel>

    companion object {
        private const val BASE_URL = "http://43.201.122.215:8080/"

        fun create(): RetrofitApi {
            val gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(RetrofitApi::class.java)
        }
    }

}