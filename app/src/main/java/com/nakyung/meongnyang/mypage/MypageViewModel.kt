package com.nakyung.meongnyang.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.nakyung.meongnyang.App
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageViewModel: ViewModel() {

    val retrofit = RetrofitApi.create()

    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

    private var username = MutableLiveData<String>()
    val nameData: LiveData<String>
    get() = username

    private var userImg = MutableLiveData<String>()
    val userData: LiveData<String>
    get() = userImg

    private var petImg = MutableLiveData<String>()
    val petdata : LiveData<String>
    get() = petImg

    val dayCount : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val withCount : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petName : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petBirth : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petSex : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val petSpec : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        viewModelScope.launch {
            var memberId = App.prefs.getInt("memberId", 0)
            var conimalId = App.prefs.getInt("conimalId", 0)

            retrofit.getMember(memberId).enqueue(object : Callback<allPet> {
                override fun onResponse(call: Call<allPet>, response: Response<allPet>) {
                    username.value = response.body()!!.nickname
                    userImg.value = response.body()!!.memberImg
                }

                override fun onFailure(call: Call<allPet>, t: Throwable) {
                    Log.d("mypage", t.message.toString())
                }
            })

            retrofit.getPet(conimalId).enqueue(object : Callback<PetModel> {
                override fun onResponse(
                    call: Call<PetModel>,
                    response: Response<PetModel>
                ) {
                    var pet = response.body()!!
                    petBirth.value = pet.birth
                    dayCount.value = pet.ddayadopt.toString()
                    withCount.value = pet.ddaybirth.toString()
                    petName.value = pet.name
                    petSex.value = pet.gender
                    petSpec.value = pet.speciesName
                    petImg.value =
                        "https://meongnyang.s3.ap-northeast-2.amazonaws.com/conimal/pet_profile.png"
                }

                override fun onFailure(call: Call<PetModel>, t: Throwable) {
                    Log.d("mypage", t.message.toString())
                }
            })

//            fbFirestore!!.collection("users").document(uid).get()
//                .addOnSuccessListener { documentsSnapshot ->
//                    var id = documentsSnapshot.toObject<Id>()!!
//                    Log.d("memberid", id.memberId.toString())
//
//                    retrofit.getMember(id.memberId!!).enqueue(object : Callback<allPet> {
//                        override fun onResponse(call: Call<allPet>, response: Response<allPet>) {
//                            username.value = response.body()!!.nickname
//                            userImg.value = response.body()!!.memberImg
//                            var conimal = response.body()!!.conimals
//                            dayCount.value = conimal[0].ddayadopt.toString()
//                            withCount.value = conimal[0].ddayadopt.toString()
//                            petName.value = conimal[0].name
//                            petSex.value = conimal[0].gender
//                            petSpec.value = conimal[0].species
//                            petImg.value =
//                                "https://meongnyang.s3.ap-northeast-2.amazonaws.com/conimal/pet_profile.png"
//                        }
//
//                        override fun onFailure(call: Call<allPet>, t: Throwable) {
//                            Log.d("mypage", t.message.toString())
//                        }
//                    })
//
//                    retrofit.getPet(id.conimalId!!).enqueue(object : Callback<PetModel> {
//                        override fun onResponse(
//                            call: Call<PetModel>,
//                            response: Response<PetModel>
//                        ) {
//                            petBirth.value = response.body()!!.birth
//                        }
//
//                        override fun onFailure(call: Call<PetModel>, t: Throwable) {
//                            Log.d("mypage", t.message.toString())
//                        }
//                    })
//
//                }
        }
    }
    fun updateNickname(nickname: String) {
        username.value = nickname
    }
    fun updateUserImg(img: String) {
        userImg.value = img
    }
    fun updatePetImg(img: String) {
        petImg.value = img
    }
}