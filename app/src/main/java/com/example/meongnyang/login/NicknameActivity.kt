package com.example.meongnyang.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.model.PostUser
import com.example.meongnyang.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NicknameActivity : AppCompatActivity() {
    private lateinit var nickEdit: EditText
    private lateinit var saveBtn: Button
    private lateinit var passEdit: EditText

    // 인터페이스 연결
    val retrofit = RetrofitApi.create()
    var memberId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_nickname)

        val intent = intent
        var email = intent.getStringExtra("email")

        nickEdit = findViewById(R.id.nickNameEdit)
        saveBtn = findViewById(R.id.nickSaveBtn)
        passEdit = findViewById(R.id.passEdit)

        // 닉네임 저장, 반려동물 등록 화면으로
        saveBtn.setOnClickListener {
            var nickname = nickEdit.text.toString()
            var pass = passEdit.text.toString()

            // 서버에 유저 정보 등록
            val user = PostUser(nickname, email!!, pass)

            retrofit.userSignUp(user).enqueue(object: Callback<UserModel> {
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    if (response.isSuccessful) {
                        Log.d("Post", response.body().toString()) // 반환값 로그로 찍어 보기
                        Log.d("Post", response.body()!!.memberId.toString())
                        memberId = response.body()!!.memberId // 할당된 고유 멤버아이디

                        val intent = Intent(this@NicknameActivity, TypeActivity::class.java)
                        intent.putExtra("memberId", memberId)
                        startActivity(intent)

                    } else {
                        Log.d("Post", "success, but ${response.errorBody()}")
                    }
                }
                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Log.d("Post", "fail ${t}")
                }
            })
        }
    }
}
