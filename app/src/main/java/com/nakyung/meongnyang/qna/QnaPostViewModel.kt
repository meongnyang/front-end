package com.nakyung.meongnyang.qna

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.model.QnaModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QnaPostViewModel(qnaId: Int) : ViewModel() {
    val retrofit = RetrofitApi.create()

    val question : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val answer : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        viewModelScope.launch {
            retrofit.getQna(qnaId).enqueue(object : Callback<QnaModel> {
                override fun onResponse(call: Call<QnaModel>, response: Response<QnaModel>) {
                   question.value = response.body()!!.question
                   answer.value = response.body()!!.answer
                }

                override fun onFailure(call: Call<QnaModel>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}