package com.example.meongnyang.diary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.model.DiaryModel
import com.example.meongnyang.model.PostDiary
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryViewModel(recordId: Int) : ViewModel() {

    val meal : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val voiding : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val voidReason : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val excretion : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val excReason : MutableLiveData<String> by lazy { MutableLiveData<String>() }


    init {
        viewModelScope.launch {
            val retrofit = RetrofitApi.create()

            retrofit.showDiary(recordId).enqueue(object : Callback<DiaryModel> {
                override fun onResponse(call: Call<DiaryModel>, response: Response<DiaryModel>) {
                    val diary = response.body()!!
                    meal.value = mealToStr(diary.meal)
                    voiding.value = voidingToStr(diary.voiding)
                    voidReason.value = diary.voiding_reason
                    excretion.value = excretionToStr(diary.excretion)
                    excReason.value = diary.excretion_reason
                }

                override fun onFailure(call: Call<DiaryModel>, t: Throwable) {

                }
            })
        }
    }

    // 숫자 -> 문자열로 표현 (db에서 가져올 때 필요하니까...)
    private fun mealToStr(meal: Int): String {
        var mealStr = ""
        when (meal) {
            1 -> {
                mealStr = "잘 먹음"
            }
            2 -> {
                mealStr = "평소보다 적음"
            }
            3 -> {
                mealStr = "안 먹음"
            }
        }
        return mealStr
    }

    private fun voidingToStr(voiding: Int): String {
        var strVoiding = ""
        when (voiding) {
            1 -> {
                strVoiding = "좋음"
            }
            2 -> {
                strVoiding = "나쁨"
            }
        }
        return strVoiding
    }

    private fun excretionToStr(excretion: Int): String {
        var strExc = ""
        when (excretion) {
            1 -> {
                strExc = "좋음"
            }
            2 -> {
                strExc = "나쁨"
            }
        }
        return strExc
    }
}