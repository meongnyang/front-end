package com.nakyung.meongnyang.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.model.GetPosts
import com.nakyung.meongnyang.model.PetModel
import com.nakyung.meongnyang.App
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    val retrofit = RetrofitApi.create()

    val name : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val strType : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val count : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val popularDog : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val popularCat : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val popularDogCnt: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val popularCatCnt: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        var conimalId = App.prefs.getInt("conimalId", 0)
        Log.d("coimal", conimalId.toString())
        viewModelScope.launch {
            retrofit.getPet(conimalId).enqueue(object : Callback<PetModel> {
                override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                    name.value = response.body()!!.name
                    strType.value = strType(response.body()!!.type)
                    count.value = response.body()!!.ddayadopt.toString()
                }

                override fun onFailure(call: Call<PetModel>, t: Throwable) {
                    Log.d("error", t.toString())
                }
            })

            retrofit.getPopularPost(1).enqueue(object : Callback<GetPosts> {
                override fun onFailure(call: Call<GetPosts>, t: Throwable) {

                }

                override fun onResponse(call: Call<GetPosts>, response: Response<GetPosts>) {
                    if (response.body() != null) {
                        popularDog.value = response.body()!!.img
                        popularDogCnt.value = response.body()!!.count.toString()
                    } else {
                        popularDog.value = ""
                        popularDogCnt.value = "0"
                    }
                }
            })

            retrofit.getPopularPost(2).enqueue(object : Callback<GetPosts> {
                override fun onFailure(call: Call<GetPosts>, t: Throwable) {
                    popularCat.value = ""
                }

                override fun onResponse(call: Call<GetPosts>, response: Response<GetPosts>) {
                    if (response.body() != null) {
                        popularCat.value = response.body()!!.img
                        popularCatCnt.value = response.body()!!.count.toString()
                    } else {
                        popularCat.value = ""
                        popularCatCnt.value = "0"
                    }
                }
            })
        }
    }

    // 숫자를 견, 묘로 바꿔주는 함수
    fun strType(type: Int): String {
        var strType = ""
        strType = if (type == 1) {
            "견"
        } else "묘"

        return strType
    }
}
