package com.nakyung.meongnyang.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.model.Id
import com.nakyung.meongnyang.model.PetModel
import com.nakyung.meongnyang.model.Score
import com.nakyung.meongnyang.model.Walk
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.nakyung.meongnyang.App
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel(la: Double, lo: Double, district: String): ViewModel() {
    val retrofit = RetrofitApi.create()

    val index : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val temperature : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val explanation : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pm10 : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pm25 : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val o3: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val o3exp: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pm10exp: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pm25exp: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val weather: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val img: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    init {
        viewModelScope.launch {
            var conimalId = App.prefs.getInt("conimalId", 0)

            retrofit.getPet(conimalId).enqueue(object : Callback<PetModel> {
                override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                    var species = response.body()!!.speciesName
                    val location = Walk(la.toInt().toString(), lo.toInt().toString(), species)
                    retrofit.walkScore(response.body()!!.category, location).enqueue(object :
                        Callback<Score> {
                        override fun onResponse(call: Call<Score>, response: Response<Score>) {
                            if (response.code() == 404 || response.code() == 500) {
                                index.value = "-"
                                explanation.value = "산책지수를 불러들이는 데에 실패했어요. 😿"
                                temperature.value = "-"
                                o3.value = "-"
                                pm10.value = "-"
                                pm25.value = "-"
                                o3exp.value = "-"
                                pm10exp.value = "-"
                                pm25exp.value = "-"
                                weather.value = "-"
                                img.value = R.drawable.fail
                            } else {
                                val walk = response.body()!!
                                index.value = walk.index
                                when (walk.index) {
                                    "아주 좋음" -> {
                                        img.value = R.drawable.perfect
                                    }
                                    "좋음" -> {
                                        img.value = R.drawable.nice
                                    }
                                    "보통" -> {
                                        img.value = R.drawable.normal
                                    }
                                    "나쁨" -> {
                                        img.value = R.drawable.bad
                                    }
                                    else -> {
                                        img.value = R.drawable.nope
                                    }
                                }
                                explanation.value = walk.explanation
                                temperature.value = "${walk.temperature}℃"
                                o3.value = walk.o3.toString()
                                pm10.value = walk.pm10.toString()
                                pm25.value = walk.pm25.toString()
                                o3exp.value = walk.o3exp
                                pm10exp.value = walk.pm10exp
                                pm25exp.value = walk.pm25exp
                                weather.value = walk.weather
                            }
                        }

                        override fun onFailure(call: Call<Score>, t: Throwable) {
                            Log.d("error", "fail")
                            Log.d("why", t.message.toString())
                        }
                    })
                }

                override fun onFailure(call: Call<PetModel>, t: Throwable) {
                }
            })
        }
    }
}