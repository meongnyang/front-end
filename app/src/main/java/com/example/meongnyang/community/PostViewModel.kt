package com.example.meongnyang.community

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel(postId: Int) : ViewModel() {
    val retrofit = RetrofitApi.create()

    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

    val title : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val contents : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val like : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val date : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val writer: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        viewModelScope.launch {
            retrofit.getPost(postId).enqueue(object : Callback<GetPosts> {
                override fun onResponse(call: Call<GetPosts>, response: Response<GetPosts>) {
                    title.value = response.body()!!.title
                    contents.value = response.body()!!.contents
                    like.value = response.body()!!.count.toString()
                    date.value = response.body()!!.date
                    writer.value = response.body()!!.nickname
                }

                override fun onFailure(call: Call<GetPosts>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}