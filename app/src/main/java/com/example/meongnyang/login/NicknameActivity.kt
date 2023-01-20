package com.example.meongnyang.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.meongnyang.R
import com.example.meongnyang.mypage.ChoiceActivity

class NicknameActivity : AppCompatActivity() {
    private lateinit var nickEdit: EditText
    private lateinit var saveBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_nickname)

        nickEdit = findViewById(R.id.nickNameEdit)
        saveBtn = findViewById(R.id.nickSaveBtn)

        // 닉네임 저장, 반려동물 등록 화면으로
        saveBtn.setOnClickListener {
            var nickname = nickEdit.text
            val intent = Intent(this, TypeActivity::class.java)
            startActivity(intent)
        }
    }
}