package com.example.meongnyang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.meongnyang.databinding.ActivityNaviBinding
import com.example.meongnyang.community.CommuFragment
import com.example.meongnyang.eye.EyeMainActivity
import com.example.meongnyang.feed.FeedFragment
import com.example.meongnyang.health.*
import com.example.meongnyang.home.HomeFragment
import com.example.meongnyang.map.MapActivity
import com.example.meongnyang.mypage.MyFragment
import com.example.meongnyang.qna.QnaFragment
import com.example.meongnyang.skin.SkinMainActivity
import com.example.meongnyang.weather.WeatherActivity

class NaviActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNaviBinding
    private lateinit var hospitalBtn: TextView
    private lateinit var skinBtn: TextView
    private lateinit var qnaBtn: TextView
    private lateinit var feedBtn: TextView
    private lateinit var healthDiaryBtn: TextView
    private lateinit var communityBtn: TextView
    private lateinit var weatherBtn: TextView
    private lateinit var eyeBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val fragName = intent.getStringExtra("fragment")

        if (fragName != null) {
            Log.d("fragment", fragName)
            when (fragName) {
                "feed" -> {
                    replace(FeedFragment())
                }
                "qna" -> {
                    replace(QnaFragment())
                }
                "community" -> {
                    replace(CommuFragment())
                }
            }
        } else {
            replace(HomeFragment())
        }

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> replace(HomeFragment())
                R.id.healthFragment -> replace(HealthFragment())
                R.id.myFragment -> replace(MyFragment())
                R.id.communityFragment -> replace(CommuFragment())
            }
            true
        }

        val menuBtn = findViewById<RelativeLayout>(R.id.menu_btn)
        val sideMenu = findViewById<DrawerLayout>(R.id.side_menu)

        menuBtn.setOnClickListener {
            sideMenu.openDrawer(GravityCompat.END)
        }

        // 버튼 정의
        hospitalBtn = findViewById(R.id.hospitalMap)
        skinBtn = findViewById(R.id.skinCheck)
        qnaBtn = findViewById(R.id.qna)
        feedBtn = findViewById(R.id.feed)
        healthDiaryBtn = findViewById(R.id.healthDiary)
        communityBtn = findViewById(R.id.community)
        weatherBtn = findViewById(R.id.weahter)
        eyeBtn = findViewById(R.id.eyeCheck)

        // 사이드 메뉴 클릭 시
        skinBtn.setOnClickListener {
            val intent = Intent(this, SkinMainActivity::class.java)
            startActivity(intent)
        }
        hospitalBtn.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
        qnaBtn.setOnClickListener {
            closeMenu()
            replace(QnaFragment())
        }
        feedBtn.setOnClickListener {
            closeMenu()
            replace(FeedFragment())
        }
        healthDiaryBtn.setOnClickListener {
            closeMenu()
            replace(HealthFragment())
        }
        communityBtn.setOnClickListener {
            closeMenu()
            replace(CommuFragment())
        }
        weatherBtn.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
        }
        eyeBtn.setOnClickListener {
            val intent = Intent(this, EyeMainActivity::class.java)
            startActivity(intent)
        }
    }

    // 사이드 메뉴 닫힘 함수
    private fun closeMenu() {
        val drawer : DrawerLayout = findViewById(R.id.side_menu)
        drawer.closeDrawer(Gravity.RIGHT)
    }

    fun replace(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment).commit()
    }

    fun refresh(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.detach(fragment).attach(fragment).commit()
    }
}