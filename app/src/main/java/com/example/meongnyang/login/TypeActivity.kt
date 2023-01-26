package com.example.meongnyang.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.meongnyang.R

class TypeActivity : AppCompatActivity() {
    private lateinit var dogBtn: Button
    private lateinit var catBtn: Button
    private lateinit var selectBtn: Button
    var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_type)

        val intent = intent
        var memberId = intent.getIntExtra("memberId", 0) // 아무것도 없으면 그냥 0 집어넣기
        Log.d("memberid", memberId.toString())

        dogBtn = findViewById(R.id.dogBtn)
        catBtn = findViewById(R.id.catBtn)
        selectBtn = findViewById(R.id.selectTypeBtn)


        dogBtn.setOnClickListener {
            dogBtn.isSelected = true
            catBtn.isSelected = false
            type = 1
        }
        catBtn.setOnClickListener {
            catBtn.isSelected = true
            dogBtn.isSelected = false
            type = 2
        }

        selectBtn.setOnClickListener {
            if (dogBtn.isSelected) {
                Log.d("type", type.toString())
                val typeintent = Intent(this, EnrollActivity::class.java)
                typeintent.putExtra("type", type)
                typeintent.putExtra("memberId", memberId)
                startActivity(typeintent)
            } else {
                Log.d("type", type.toString())
                val typeintent = Intent(this, CatEnrollActivity::class.java)
                typeintent.putExtra("type", type)
                typeintent.putExtra("memberId", memberId)
                startActivity(typeintent)
            }
        }
    }
}