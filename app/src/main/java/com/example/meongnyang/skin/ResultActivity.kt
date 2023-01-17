package com.example.meongnyang.skin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.databinding.FragmentHealthBinding
import com.example.meongnyang.databinding.SkinActivityResultBinding
import com.example.meongnyang.feed.FeedFragment
import com.example.meongnyang.home.HomeFragment
import com.example.meongnyang.map.MapActivity
import com.example.meongnyang.qna.QnaFragment

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: SkinActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.skin_activity_result)

        // 이미지 띄우기
        val intent = intent
        val bytes = intent.getByteArrayExtra("image")
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
        binding.resultImg.setImageBitmap(bitmap)

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