package com.example.meongnyang.mypage

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.login.NicknameActivity
import com.example.meongnyang.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MypageViewModel: ViewModel() {

    val retrofit = RetrofitApi.create()

    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

    val username : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val dayCount : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val withCount : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petName : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petBirth : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petSex : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petSpec : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        viewModelScope.launch {
            fbFirestore!!.collection("users").document(uid).get()
                .addOnSuccessListener { documentsSnapshot ->
                    var id = documentsSnapshot.toObject<Id>()!!
                    Log.d("memberid", id.memberId.toString())

                    retrofit.getMember(id.memberId!!).enqueue(object : Callback<allPet> {
                        override fun onResponse(call: Call<allPet>, response: Response<allPet>) {
                            username.value = response.body()!!.nickname
                            var conimal = response.body()!!.conimals
                            dayCount.value = conimal[0].ddayadopt.toString()
                            withCount.value = conimal[0].ddayadopt.toString()
                            petName.value = conimal[0].name
                            petSex.value = conimal[0].gender
                            petSpec.value = conimal[0].species
                        }

                        override fun onFailure(call: Call<allPet>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })

                    retrofit.getPet(id.conimalId!!).enqueue(object : Callback<PetModel> {
                        override fun onResponse(
                            call: Call<PetModel>,
                            response: Response<PetModel>
                        ) {
                            petBirth.value = response.body()!!.birth
                        }

                        override fun onFailure(call: Call<PetModel>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }
        }
    }
}