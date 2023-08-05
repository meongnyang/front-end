package com.nakyung.meongnyang.skin

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.nakyung.meongnyang.NaviActivity
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.databinding.SkinActivityResultBinding
import com.nakyung.meongnyang.map.MapActivity
import com.nakyung.meongnyang.model.Name
import com.nakyung.meongnyang.model.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: SkinActivityResultBinding
    var resultName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.skin_activity_result)

        // 이미지 띄우기
        val intent = intent
        val bytes = intent.getByteArrayExtra("image")
        resultName = intent.getStringExtra("result").toString()
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
        binding.resultImg.setImageBitmap(bitmap)

        Log.d("resultName", resultName)

        val retrofit = RetrofitApi.create()

        var result = Name(resultName)

        if (resultName == "noResult") {
            binding.resultTitle.text = "의심되는 피부 질환이 발견되지 않아요"
            binding.resultName.text = "앞으로도 피부 관리에 힘써주세요 👏"
            binding.reasonText.text = "-"
            binding.manageText.text = "-"
        } else {
            retrofit.getDisease(result).enqueue(object : Callback<Result> {
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    if (response.isSuccessful) {
                        binding.resultTitle.text = "피부 질환이 있는 것 같아요"
                        binding.resultName.text = "⚠️ ${response.body()!!.name}(으)로 의심돼요 ⚠️"
                        binding.reasonText.text = response.body()!!.reason
                        binding.manageText.text = response.body()!!.manage
                    }
                }
                override fun onFailure(call: Call<Result>, t: Throwable) {
                    Log.d("error", t.message.toString())

                }
            })
        }

        // 메뉴 클릭 시 이동
        binding.goFeedBtn.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
            intent.putExtra("fragment", "feed")
            startActivity(intent)
        }
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