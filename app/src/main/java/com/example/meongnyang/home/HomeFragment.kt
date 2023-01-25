package com.example.meongnyang.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.community.CommuFragment
import com.example.meongnyang.databinding.FragmentHomeBinding
import com.example.meongnyang.diary.DiaryFragment
import com.example.meongnyang.feed.FeedFragment
import com.example.meongnyang.qna.QnaFragment
import com.example.meongnyang.map.PlayMapActivity
import com.example.meongnyang.skin.SkinMainActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val model: HomeViewModel by activityViewModels()

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
        binding.playMapMenu.setOnClickListener {
            val intent = Intent(context, PlayMapActivity::class.java)
            startActivity(intent)
        }
        binding.hospitalMenu.setOnClickListener {
            val intent = Intent(context, com.example.meongnyang.map.MapActivity::class.java)
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

        return binding?.root
    }

    fun strType(type: Int): String {
        var strType = ""
        strType = if (type == 1) {
            "견"
        } else "묘"

        return strType
    }
}