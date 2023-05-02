package com.example.meongnyang.weather

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.model.Id
import com.example.meongnyang.model.PetModel
import com.example.meongnyang.model.Score
import com.example.meongnyang.model.Walk
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel(la: Double, lo: Double, district: String): ViewModel() {
    val retrofit = RetrofitApi.create()

    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

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

    init {
        viewModelScope.launch {
            val location = Walk(la.toInt().toString(), lo.toInt().toString(), "서울", district)
            fbFirestore!!.collection("users").document(uid).get()
                .addOnSuccessListener { documentsSnapshot ->
                    var id = documentsSnapshot.toObject<Id>()!!

                    retrofit.getPet(id.conimalId!!).enqueue(object: Callback<PetModel> {
                        override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                            retrofit.walkScore(response.body()!!.category, location).enqueue(object:
                                Callback<Score> {
                                override fun onResponse(call: Call<Score>, response: Response<Score>) {
                                    if (response.code() == 404) {
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
                                    } else {
                                        index.value = response.body()!!.index
                                        explanation.value = response.body()!!.explanation
                                        temperature.value = "${response.body()!!.temperature}℃"
                                        o3.value = response.body()!!.o3.toString()
                                        pm10.value = response.body()!!.pm10.toString()
                                        pm25.value = response.body()!!.pm25.toString()
                                        o3exp.value = response.body()!!.o3exp
                                        pm10exp.value = response.body()!!.pm10exp
                                        pm25exp.value = response.body()!!.pm25exp
                                        weather.value = response.body()!!.weather
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
}