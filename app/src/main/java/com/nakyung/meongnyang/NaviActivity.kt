package com.nakyung.meongnyang

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
import com.nakyung.meongnyang.databinding.ActivityNaviBinding
import com.nakyung.meongnyang.community.CommuFragment
import com.nakyung.meongnyang.eye.EyeMainActivity
import com.nakyung.meongnyang.feed.FeedFragment
import com.nakyung.meongnyang.health.*
import com.nakyung.meongnyang.home.HomeFragment
import com.nakyung.meongnyang.map.MapActivity
import com.nakyung.meongnyang.mypage.MyFragment
import com.nakyung.meongnyang.qna.QnaFragment
import com.nakyung.meongnyang.skin.SkinMainActivity
import com.nakyung.meongnyang.weather.WeatherActivity

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
            val skin = Intent(this, SkinMainActivity::class.java)
            startActivity(skin)
        }
        hospitalBtn.setOnClickListener {
            val hospital = Intent(this, MapActivity::class.java)
            startActivity(hospital)
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
            val weather = Intent(this, WeatherActivity::class.java)
            startActivity(weather)
        }
        eyeBtn.setOnClickListener {
            val eye = Intent(this, EyeMainActivity::class.java)
            startActivity(eye)
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