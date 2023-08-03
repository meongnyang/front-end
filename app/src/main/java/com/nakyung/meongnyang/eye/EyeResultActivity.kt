package com.nakyung.meongnyang.eye

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.nakyung.meongnyang.NaviActivity
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.databinding.EyeActivityResultBinding
import com.nakyung.meongnyang.map.MapActivity
import com.nakyung.meongnyang.model.Name
import com.nakyung.meongnyang.model.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EyeResultActivity : AppCompatActivity() {
    private lateinit var binding: EyeActivityResultBinding
    var result = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.eye_activity_result)

        // 이미지 띄우기
        val intent = intent
        val bytes = intent.getByteArrayExtra("image")
        result = intent.getStringExtra("result").toString()
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
        binding.eyeResultImg.setImageBitmap(bitmap)

        Log.d("pet", result)

        val retrofit = RetrofitApi.create()

        var result = Name(result)

        retrofit.getDisease(result).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.isSuccessful) {
                    binding.eyeResultTitle.text = "안구 질환이 있는 것 같아요"
                    binding.eyeResultName.text = "⚠️ ${response.body()!!.name}(으)로 의심돼요 ⚠️"
                    binding.reasonTextEye.text = response.body()!!.reason
                    binding.manageTextEye.text = response.body()!!.manage
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d("error", t.message.toString())

            }
        })

        // 메뉴 클릭 시 이동
        binding.goMainBtn.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }
        binding.goMapBtn.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
        binding.goQnaBtn.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
            intent.putExtra("fragment", "qna")
            startActivity(intent)
        }
    }
}