package com.example.meongnyang.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.meongnyang.App
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.home.HomeFragment
import com.example.meongnyang.model.*
import com.example.meongnyang.skin.SkinMainActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {
    private lateinit var ggBtn: Button
    private lateinit var kkoBtn: Button
    private lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit

    private var auth: FirebaseAuth? = null // 객체의 공유 인스턴스
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ggBtn = findViewById(R.id.ggBtn)
        kkoBtn = findViewById(R.id.kkoBtn)

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

        // 카카오 로그인 유지하기
//        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
//            if (error != null) {
//
//            } else if (tokenInfo != null) {
//                // 홈 화면으로 넘어가기
//                var intent = Intent(this, NaviActivity::class.java)
//                startActivity(intent)
//            }
//        }

//        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//            if (error != null) {
//                when {
//                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
//                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
//                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
//                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
//                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
//                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
//                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.ServerError.toString() -> {
//                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
//                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
//                    }
//                    else -> { // Unknown
//                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } else if (token != null) {
//                Toast.makeText(this, "멍냥백서 가입 성공!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, NicknameActivity::class.java)
//                var mail = kkoEmail()
//                intent.putExtra("email", mail)
//                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                finish()
//            }
//        }
//
//        // 카카오 로그인 버튼 클릭했을 시
//        kkoBtn.setOnClickListener {
//            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
//                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
//            } else {
//                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
//            }
//        }
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
            var intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
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

//    private fun kkoEmail(): String {
//        var email = ""
//        // 사용자 정보 요청
//        UserApiClient.instance.me { user, error ->
//            if (error != null) {
//
//            } else if (user != null) {
//                email = user.kakaoAccount?.email.toString()
//            }
//        }
//        return email
//    }

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