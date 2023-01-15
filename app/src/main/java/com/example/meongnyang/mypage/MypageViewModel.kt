package com.example.meongnyang.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meongnyang.model.Pet
import com.example.meongnyang.model.User
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MypageViewModel: ViewModel() {
    val username : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val dayCount : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val withCount : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userImg : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petName : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petBirth : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petSex : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petSpec : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petImg : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val user = User("만두언니","abc@naver.com",  "1234", "http://domain/image/profile.png")
    private val pet = Pet(1,1,  "정만두", "여아", "2019-03-16", "2019-06-14", "비숑프리제", "http://domain/image/profile.png")

    init {
        viewModelScope.launch {
            username.value = user.nickname
            dayCount.value = (dayCount(pet.birth))
            withCount.value = (withCount(pet.adopt))
            userImg.value = user.profile
            petName.value = pet.name
            petBirth.value = pet.birth
            petSex.value = pet.sex
            petSpec.value = pet.species
            petImg.value = pet.url
        }
    }

    // 태어난 지 날짜 카운트
    private fun dayCount(birth: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val birthDate = formatter.parse(birth)

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

        val diff = today - birthDate.time
        val count = (diff / (24*60*60*1000) + 1).toString()

        return count
    }

    // 입양 후 날짜 카운트 함수
    private fun withCount(adopt: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val adoptDate = formatter.parse(adopt)

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

        val diff = today - adoptDate.time
        val count = (diff / (24 * 60 * 60 * 1000) + 1).toString()

        return count
    }
}