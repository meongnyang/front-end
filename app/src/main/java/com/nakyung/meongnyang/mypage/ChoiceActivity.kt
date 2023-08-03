package com.nakyung.meongnyang.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.nakyung.meongnyang.R

class ChoiceActivity : AppCompatActivity() {
    private lateinit var dogBtn: Button
    private lateinit var catBtn: Button
    private lateinit var selectBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage_activity_choice)

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

        // 어떤 것 선택하느냐에 따라 강아지, 고양이 intent로 전달?
        selectBtn.setOnClickListener {
            if (dogBtn.isSelected) {
                val intent = Intent(this, AddActivity::class.java)
                intent.putExtra("type", "강아지")
                startActivity(intent)
            } else {
                val intent = Intent(this, AddActivity::class.java)
                intent.putExtra("type", "고양이")
                startActivity(intent)
            }
        }
    }
}