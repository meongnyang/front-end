package com.example.meongnyang.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meongnyang.model.Pet
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {
    val name : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val strType : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val count : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val pet = Pet(1,1,  "정만두", "여아", "2019-03-16", "2019-06-14", "비숑프리제", "http://domain/image/profile.png")

    init {
        viewModelScope.launch {
            name.value = pet.name
            strType.value = strType(pet.type)
            count.value = dayCount(pet.adopt)
        }
    }

    // 함수는 다른 곳에 만들어 주기
    // 숫자를 견, 묘로 바꿔주는 함수
    private fun strType(type: Int): String {
        var strType = ""
        strType = if (type == 1) {
            "견"
        } else "묘"

        return strType
    }

    // 입양 후 날짜 카운트 함수
    private fun dayCount(adopt: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val adoptDate = formatter.parse(adopt)

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

        val diff = today - adoptDate.time
        val count = (diff / (24*60*60*1000) + 1).toString()

        return count
    }
}
