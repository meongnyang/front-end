package com.example.meongnyang.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.home.HomeViewModel
import com.example.meongnyang.model.BaseResponse
import com.example.meongnyang.model.Id
import com.example.meongnyang.model.PetModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*class Repository {
    /*private val client = RetrofitApi.create()
    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()
    var memberId = 0

    fun getId(): Id {
        var id = Id()
        fbFirestore!!.collection("users").document(uid).get()
            .addOnSuccessListener { documentsSnapshot ->
                id = documentsSnapshot.toObject<Id>()!!
            }
        return id
    }


    fun getPet(
        successCallback: (PetModel) -> Unit,
        failCallback: ((String) -> Unit)? = null,
        statusCallback: ((Int, Boolean, String) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        client.getPet(getId()).enqueue(
            createFromRemoteCallback(
                mapper = {
                    it.name
                },
                successCallback = successCallback,
                failCallback = failCallback,
                statusCallback = statusCallback,
                errorCallback, errorCallback
            )
        )
    }

    private fun <UI, SERVER: BaseResponse<*>>createFromRemoteCallback(
        mapper: (SERVER) -> UI,
        successCallback: ((UI) -> Unit)? = null,
        failCallback: ((String) -> Unit)? = null,
        statusCallback: ((Int, Boolean, String) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ): Callback<SERVER>
    {
        return object : Callback<SERVER> {
            override fun onFailure(call: Call<SERVER>, t: Throwable) {
                errorCallback?.invoke(t)
            }

            override fun onResponse(
                call: Call<SERVER>,
                response: Response<SERVER>
            ) {
                response.body()?.let {
                    statusCallback?.invoke(it.status, it.success, it.message)
                    if (it.success)
                        successCallback?.invoke(mapper(it))
                    else if (!it.success) {
                        failCallback?.invoke(it.message)
                    } else {

                    }
                }
            }

        }
    }
    //private val client = RetrofitApi.create()
    //var pet = PetModel()

    /*fun getPet(): PetModel {
        Log.d("pet", client.getPet(1).toString())
        client.getPet(1).enqueue(object: Callback<PetModel> {
            override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                TODO("Not yet implemented")
            }
        })
    }*/

    /*fun getData(): PetModel {
        client.getPet(1).enqueue(object: Callback<PetModel> {
            override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                if (response.isSuccessful) {
                    pet = response.body()!!
                }
                return pet
            }
            override fun onFailure(call: Call<PetModel>, t: Throwable) {
                Log.d("error", t.toString())
            }
        })
    }*/
}*/