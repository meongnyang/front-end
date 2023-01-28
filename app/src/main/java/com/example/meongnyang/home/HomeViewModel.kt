package com.example.meongnyang.home

import android.util.Log
import androidx.lifecycle.*
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.model.Id
import com.example.meongnyang.model.PetModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {
    val retrofit = RetrofitApi.create()

    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

    val name : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val strType : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val count : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        viewModelScope.launch {
            var id = Id()
            fbFirestore!!.collection("users").document(uid).get()
                .addOnSuccessListener { documentsSnapshot ->
                    id = documentsSnapshot.toObject<Id>()!!

                    retrofit.getPet(id.conimalId!!).enqueue(object : Callback<PetModel> {
                        override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                            name.value = response.body()!!.name
                            strType.value = strType(response.body()!!.type)
                            count.value = response.body()!!.ddayadopt.toString()
                        }

                        override fun onFailure(call: Call<PetModel>, t: Throwable) {
                            Log.d("error", t.toString())
                        }
                    })
                }
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
