package com.example.meongnyang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.meongnyang.databinding.ActivityNaviBinding
import com.example.meongnyang.community.CommuFragment
import com.example.meongnyang.diary.DiaryFragment
import com.example.meongnyang.feed.FeedFragment
import com.example.meongnyang.health.*
import com.example.meongnyang.home.HomeFragment
import com.example.meongnyang.map.MapActivity
import com.example.meongnyang.map.PlayMapActivity
import com.example.meongnyang.mypage.MyFragment
import com.example.meongnyang.qna.QnaFragment
import com.example.meongnyang.skin.CameraActivity

class NaviActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNaviBinding
    private lateinit var hospitalBtn: TextView
    private lateinit var skinBtn: TextView
    private lateinit var playBtn: TextView
    private lateinit var qnaBtn: TextView
    private lateinit var feedBtn: TextView
    private lateinit var healthDiaryBtn: TextView
    private lateinit var communityBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> replaceFragment(HomeFragment())
                R.id.healthFragment -> replaceFragment(HealthFragment())
                R.id.myFragment -> replaceFragment(MyFragment())
                R.id.communityFragment -> replaceFragment(CommuFragment())
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
        playBtn = findViewById(R.id.playMap)
        qnaBtn = findViewById(R.id.qna)
        feedBtn = findViewById(R.id.feed)
        healthDiaryBtn = findViewById(R.id.healthDiary)
        communityBtn = findViewById(R.id.community)

        // 사이드 메뉴 클릭 시
        skinBtn.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
        hospitalBtn.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
        playBtn.setOnClickListener {
            val intent = Intent(this, PlayMapActivity::class.java)
            startActivity(intent)
        }
        qnaBtn.setOnClickListener {
            closeMenu()
            replaceFragment(QnaFragment())
        }
        feedBtn.setOnClickListener {
            closeMenu()
            replaceFragment(FeedFragment())
        }
        healthDiaryBtn.setOnClickListener {
            closeMenu()
            replaceFragment(DiaryFragment())
        }
        communityBtn.setOnClickListener {
            // 커뮤니티 화면으로 가긴 하는데 하단바,, 자동으로 옮기는 법 없나,,
            closeMenu()
            replaceFragment(CommuFragment())
        }
    }

    // 사이드 메뉴 닫힘 함수
    private fun closeMenu() {
        val drawer : DrawerLayout = findViewById(R.id.side_menu)
        drawer.closeDrawer(Gravity.RIGHT)
    }

    // 프래그먼트 전환 이벤트
    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment).commit()
    }
}