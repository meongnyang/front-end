package com.nakyung.meongnyang.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.nakyung.meongnyang.App
import com.nakyung.meongnyang.NaviActivity
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.model.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var ggBtn: Button

    private var auth: FirebaseAuth? = null // 객체의 공유 인스턴스
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ggBtn = findViewById(R.id.ggBtn)

        // firebase auth 객체 초기화
        auth = FirebaseAuth.getInstance()

        // 구글 계정으로 로그인 버튼 클릭 시
        ggBtn.setOnClickListener {
            ggSignIn() // 로그인 요청
        }

        // 구글 로그인 옵션 구성, Email 요청
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        client = GoogleSignIn.getClient(this, gso)
    }

    // 유저가 앱에 이미 구글 로그인을 했는지 확인하기
    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            moveNextPage()
        }
    }

    private fun ggSignIn() {
        var signInIntent = client?.signInIntent
        startActivityForResult(signInIntent, 100)
    }

    private fun moveNextPage() {
        var currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val retrofit = RetrofitApi.create()
            retrofit.loginToken(Email(currentUser?.email!!)).enqueue(object : Callback<LoginUser> {
                override fun onResponse(call: Call<LoginUser>, response: Response<LoginUser>) {
                    // 토큰 저장하기
                    App.prefs.setString("token", "Bearer ${response.body()!!.token}")
                    // memberId 저장
                    App.prefs.setInt("memberId", response.body()!!.memberId)

                    var intent = Intent(this@LoginActivity, SelectPetActivity::class.java)
                    startActivity(intent)
                    Log.d("token", App.prefs.getString("token", "no_token"))
                }
                override fun onFailure(call: Call<LoginUser>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    // Google Sign-In Methods
    private fun firebaseAuthWithGoogle(account : GoogleSignInAccount?) {
        // Google SignInAccount 객체에서 아이디 토큰을 가져와 firebase auth로 교환하고 firebase에 인증
        var credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(credential)?.addOnCompleteListener{
                task ->
            if (task.isSuccessful) {
                val retrofit = RetrofitApi.create()
                retrofit.findId(Email(account?.email!!)).enqueue(object : Callback<MemberId> {
                    override fun onFailure(call: Call<MemberId>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<MemberId>, response: Response<MemberId>) {
                        // 이미 가입한 회원일 경우
                        if (response.body() != null) {
                            // login token
                            retrofit.loginToken(Email(account?.email!!)).enqueue(object : Callback<LoginUser> {
                                override fun onResponse(
                                    call: Call<LoginUser>,
                                    response: Response<LoginUser>
                                ) {
                                    // 토큰 저장하기
                                    App.prefs.setString("token", "Bearer ${response.body()!!.token}")
                                    App.prefs.setInt("memberId", response.body()!!.memberId)

                                    var intent = Intent(this@LoginActivity, NaviActivity::class.java)
                                    startActivity(intent)
                                    Log.d("token", App.prefs.getString("token", "no_token"))
                                }

                                override fun onFailure(call: Call<LoginUser>, t: Throwable) {
                                    Log.d("login_error", response.errorBody()?.string()!!)
                                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_LONG).show()
                                }
                            })

                        } else {
                            // 회원가입 시키기
                            Toast.makeText(this@LoginActivity, "멍냥백서 가입 성공!", Toast.LENGTH_LONG).show()
                            var intent = Intent(this@LoginActivity, NicknameActivity::class.java)
                            intent.putExtra("email", account?.email)
                            startActivity(intent)
                        }
                    }
                })

            } else {
                // 오류
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100) {
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            // result가 성공했을 때 이 값을 firebase에 넘겨주기
            if(result!!.isSuccess) {
                var account = result.signInAccount
                // Second step
                firebaseAuthWithGoogle(account)
            }
        }
    }

    // Override
    override fun onResume() {
        super.onResume()
        moveNextPage() // 자동 로그인
    }
}