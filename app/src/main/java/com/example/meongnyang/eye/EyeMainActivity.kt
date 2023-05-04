package com.example.meongnyang.eye

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.meongnyang.R

class EyeMainActivity : AppCompatActivity() {
    private lateinit var startBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eye_activity_main)

        startBtn = findViewById(R.id.startBtn)

        startBtn.setOnClickListener {
            val intent = Intent(this, EyeCameraActivity::class.java)
            startActivity(intent)
        }
    }
}