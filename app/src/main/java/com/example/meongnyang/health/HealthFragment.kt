package com.example.meongnyang.health

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.databinding.FragmentHealthBinding
import com.example.meongnyang.diary.DiaryFragment
import com.example.meongnyang.diary.DiaryViewModel
import com.example.meongnyang.feed.FeedFragment
import com.example.meongnyang.qna.QnaFragment
import com.example.meongnyang.skin.CameraActivity
import com.example.meongnyang.skin.SkinMainActivity

class HealthFragment : Fragment() {
    private lateinit var binding: FragmentHealthBinding
    private val model: DiaryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_health,
            container,
            false
        )
        binding.apply {
            diary = model
            lifecycleOwner = this@HealthFragment
        }

        // 메뉴 화면 이동
        binding.skinCheckBtn.setOnClickListener {
            val intent = Intent(context, SkinMainActivity::class.java)
            startActivity(intent)
        }
        binding.qnaBtn.setOnClickListener {
            (activity as NaviActivity).replaceFragment(QnaFragment())
        }
        binding.hospitalBtn.setOnClickListener {
            val intent = Intent(context, com.example.meongnyang.map.MapActivity::class.java)
            startActivity(intent)
        }
        binding.feedBtn.setOnClickListener {
            (activity as NaviActivity).replaceFragment(FeedFragment())
        }

        // 건강기록부 작성 화면으로
        binding.diaryView.setOnClickListener {
            (activity as NaviActivity).replaceFragment(DiaryFragment())
        }

        // 건강기록부 데이터 있으면 text 없애기
        if (binding.diary != null) {
            binding.clickText.visibility = View.INVISIBLE
            binding.noDataText.visibility = View.INVISIBLE
            binding.mealText.visibility = View.VISIBLE
            binding.voidText.visibility = View.VISIBLE
            binding.excText.visibility = View.VISIBLE
        } else {
            binding.clickText.visibility = View.VISIBLE
            binding.noDataText.visibility = View.VISIBLE
            binding.mealText.visibility = View.INVISIBLE
            binding.voidText.visibility = View.INVISIBLE
            binding.excText.visibility = View.INVISIBLE
        }

        return binding?.root
    }

}