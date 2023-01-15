package com.example.meongnyang.mypage

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
import com.example.meongnyang.databinding.MypageFragmentMyBinding

class MyFragment : Fragment() {
    private lateinit var binding: MypageFragmentMyBinding
    private val model: MypageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_fragment_my,
            container,
            false
        )
        binding.apply {
            mypage = model
            lifecycleOwner = this@MyFragment
        }

        // 닉네임 변경 화면으로 이동하기
        binding.modifyProfile.setOnClickListener {
            (activity as NaviActivity).replaceFragment(ModifyFragment())
        }
        // 반려동물 추가하기
        binding.addPet.setOnClickListener {
            val intent = Intent(context, AddActivity::class.java)
            startActivity(intent)
        }
        // 메인 반려동물 변경
        binding.changePet.setOnClickListener {
            val intent = Intent(context, ChoiceActivity::class.java)
            startActivity(intent)
        }

        return binding?.root
    }
}