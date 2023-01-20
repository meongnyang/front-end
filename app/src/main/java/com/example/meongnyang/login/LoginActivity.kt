package com.example.meongnyang.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
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

class LoginActivity : AppCompatActivity() {
    private lateinit var ggBtn: Button
    private lateinit var kkoBtn: Button

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
                // 로그인 처리
                Toast.makeText(this, "멍냥백서 가입 성공!", Toast.LENGTH_LONG).show()
                var intent = Intent(this, NicknameActivity::class.java)
                startActivity(intent)
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