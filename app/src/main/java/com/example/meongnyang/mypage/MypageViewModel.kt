package com.example.meongnyang.mypage

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.login.NicknameActivity
import com.example.meongnyang.model.Pet
import com.example.meongnyang.model.PostUser
import com.example.meongnyang.model.User
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    val retrofit = RetrofitApi.create()

    // 이건 임시데이터, 서버에서 받아와야대
    private val user = User("만두언니", "http://domain/image/profile.png", "2019-03-16", "2019-06-14", "http://domain/image/profile.png", "정만두", "여아", "비숑프리제")
    init {
        viewModelScope.launch {
            username.value = user.nickname
            dayCount.value = (dayCount(user.birth))
            withCount.value = (withCount(user.adopt))
            userImg.value = user.memberimg
            petName.value = user.name
            petBirth.value = user.birth
            petSex.value = user.gender
            petSpec.value = user.species
            petImg.value = user.conimalimg
        }
    }
    // 통신해서 받아오기
    /*init {
        retrofit.getMembers(1).enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    viewModelScope.launch {
                        username.value = response.body()!!.nickname
                        dayCount.value = (dayCount(response.body()!!.birth))
                        withCount.value = (withCount(response.body()!!.adopt))
                        userImg.value = response.body()!!.memberimg
                        petName.value = response.body()!!.name
                        petBirth.value = response.body()!!.birth
                        petSex.value = response.body()!!.sex
                        petSpec.value = response.body()!!.species
                        petImg.value = response.body()!!.conimalimg
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

            }
        })
    }*/

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