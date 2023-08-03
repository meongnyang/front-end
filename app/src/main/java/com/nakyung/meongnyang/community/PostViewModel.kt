package com.nakyung.meongnyang.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.model.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel(postId: Int) : ViewModel() {
    val retrofit = RetrofitApi.create()

    val title : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val contents : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val like : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val date : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val writer: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val img: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userImg: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        viewModelScope.launch {
            retrofit.getPost(postId).enqueue(object : Callback<GetPosts> {
                override fun onResponse(call: Call<GetPosts>, response: Response<GetPosts>) {
                    title.value = response.body()!!.title
                    contents.value = response.body()!!.contents
                    like.value = response.body()!!.count.toString()
                    date.value = response.body()!!.date
                    writer.value = response.body()!!.nickname
                    img.value = response.body()!!.img
                    retrofit.getMember(response.body()!!.memberId).enqueue(object : Callback<allPet> {
                        override fun onResponse(call: Call<allPet>, response: Response<allPet>) {
                            userImg.value = response.body()!!.memberImg
                        }

                        override fun onFailure(call: Call<allPet>, t: Throwable) {

                        }
                    })
                }

                override fun onFailure(call: Call<GetPosts>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}