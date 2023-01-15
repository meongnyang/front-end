package com.example.meongnyang.diary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meongnyang.model.Diary
import kotlinx.coroutines.launch

class DiaryViewModel : ViewModel() {
    val meal : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val voiding : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val excretion : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val diary = Diary(1, 1, 1, 1, 1)

    init {
        viewModelScope.launch {
            meal.value = mealToStr(diary.meal)
            voiding.value = voidingToStr(diary.voiding)
            excretion.value = excretionToStr(diary.excretion)
        }
    }

    // 숫자 -> 문자열로 표현
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