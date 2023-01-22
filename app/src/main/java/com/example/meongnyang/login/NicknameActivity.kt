package com.example.meongnyang.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.model.PostResult
import com.example.meongnyang.model.PostUser
import com.example.meongnyang.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NicknameActivity : AppCompatActivity() {
    private lateinit var nickEdit: EditText
    private lateinit var saveBtn: Button
    // 인터페이스 연결
    val retrofit = RetrofitApi.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_nickname)

        val intent = intent
        val email = intent.getStringExtra("email")

        nickEdit = findViewById(R.id.nickNameEdit)
        saveBtn = findViewById(R.id.nickSaveBtn)

        // 닉네임 저장, 반려동물 등록 화면으로
        saveBtn.setOnClickListener {
            var nickname = nickEdit.text.toString()

            // 서버에 유저 정보 POST
            val user = PostUser(email!!, nickname, null)
            retrofit.postMembers(user).enqueue(object: Callback<UserModel> {
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    if (response.isSuccessful) {
                        Log.d("Post", "success ${response}")
                    } else {
                        Log.d("Post", "success, but ${response.errorBody()}")
                    }
                }
                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Log.d("Post", "fail ${t}")
                }
            })

            val intent = Intent(this, TypeActivity::class.java)
            startActivity(intent)
        }
    }
}
