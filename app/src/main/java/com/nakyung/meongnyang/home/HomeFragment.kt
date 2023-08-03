package com.nakyung.meongnyang.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nakyung.meongnyang.NaviActivity
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.community.CommuFragment
import com.nakyung.meongnyang.databinding.FragmentHomeBinding
import com.nakyung.meongnyang.diary.DiaryFragment
import com.nakyung.meongnyang.eye.EyeMainActivity
import com.nakyung.meongnyang.feed.FeedFragment
import com.nakyung.meongnyang.qna.QnaFragment
import com.nakyung.meongnyang.skin.SkinMainActivity
import com.nakyung.meongnyang.weather.WeatherActivity
import com.nakyung.meongnyang.weather.WeatherViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val model: HomeViewModel by activityViewModels()
    private val weatherModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        binding.apply {
            pet = model
            lifecycleOwner = this@HomeFragment
        }

        // 메뉴 클릭 이벤트
        binding.FeedMenu.setOnClickListener {
            (activity as NaviActivity).replace(FeedFragment())
        }
        binding.skinMenu.setOnClickListener {
            val intent = Intent(context, SkinMainActivity::class.java)
            startActivity(intent)
        }
        binding.eyeMenu.setOnClickListener {
            val intent = Intent(context, EyeMainActivity::class.java)
            startActivity(intent)
        }
        binding.hospitalMenu.setOnClickListener {
            val intent = Intent(context, com.nakyung.meongnyang.map.MapActivity::class.java)
            startActivity(intent)
        }
        binding.qnaMenu.setOnClickListener {
            (activity as NaviActivity).replace(QnaFragment())
        }
        binding.diaryMenu.setOnClickListener {
            (activity as NaviActivity).replace(DiaryFragment())
        }
        binding.commuMenu.setOnClickListener {
            (activity as NaviActivity).replace(CommuFragment())
        }
        binding.WeatherMenu.setOnClickListener {
            val intent = Intent(context, WeatherActivity::class.java)
            startActivity(intent)
        }

        return binding?.root
    }
}