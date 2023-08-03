package com.nakyung.meongnyang.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.nakyung.meongnyang.App
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.model.LoginUser
import com.nakyung.meongnyang.model.PostUser
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

            retrofit.userSignUp(user).enqueue(object: Callback<LoginUser> {
                override fun onResponse(call: Call<LoginUser>, response: Response<LoginUser>) {
                    if (response.isSuccessful) {
                        memberId = response.body()!!.memberId // 할당된 고유 멤버아이디

                        // 토큰 저장
                        App.prefs.setString("token", "Bearer ${response.body()!!.token}")

                        val intent = Intent(this@NicknameActivity, TypeActivity::class.java)
                        intent.putExtra("memberId", memberId)
                        startActivity(intent)

                        Log.d("token", response.body()!!.toString())

                    } else {
                        Log.d("Post", "success, but ${response.errorBody()?.string()!!}")
                    }
                }
                override fun onFailure(call: Call<LoginUser>, t: Throwable) {
                    Log.d("Post", "fail $t")
                }
            })
        }
    }
}
