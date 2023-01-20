package com.example.meongnyang.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.meongnyang.R
import com.example.meongnyang.mypage.AddActivity

class TypeActivity : AppCompatActivity() {
    private lateinit var dogBtn: Button
    private lateinit var catBtn: Button
    private lateinit var selectBtn: Button
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
                val intent = Intent(this, EnrollActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, EnrollActivity::class.java)
                startActivity(intent)
            }
        }
    }
}