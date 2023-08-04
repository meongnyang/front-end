package com.nakyung.meongnyang.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.model.GetPosts
import com.nakyung.meongnyang.model.Id
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class TypeActivity : AppCompatActivity() {
    private lateinit var dogBtn: Button
    private lateinit var catBtn: Button
    private lateinit var selectBtn: Button
    //var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_type)

        dogBtn = findViewById(R.id.dogBtn)
        catBtn = findViewById(R.id.catBtn)
        selectBtn = findViewById(R.id.selectTypeBtn)


        dogBtn.setOnClickListener {
            dogBtn.isSelected = true
            catBtn.isSelected = false
        }
        catBtn.setOnClickListener {
            catBtn.isSelected = true
            dogBtn.isSelected = false
        }

        selectBtn.setOnClickListener {
            if (dogBtn.isSelected) {
                val typeintent = Intent(this, EnrollActivity::class.java)
                //typeintent.putExtra("memberId", memberId)
                //typeintent.putExtra("isAdd", isAdd)
                startActivity(typeintent)
            } else {
                val typeintent = Intent(this, CatEnrollActivity::class.java)
                //typeintent.putExtra("memberId", memberId)
                //typeintent.putExtra("isAdd", isAdd)
                startActivity(typeintent)
            }
        }
    }
}