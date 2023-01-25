package com.example.meongnyang.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.databinding.DiaryFragmentWriteBinding
import com.example.meongnyang.health.HealthFragment
import com.example.meongnyang.mypage.KeyboardVisibilityUtils

class DiaryFragment : Fragment() {
    private lateinit var binding: DiaryFragmentWriteBinding
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.diary_fragment_write,
            container,
            false
        )

        // 식이 질문 버튼
        binding.mealBtn1.setOnClickListener {
            binding.mealBtn1.isSelected = true
            binding.mealBtn2.isSelected = false
            binding.mealBtn3.isSelected = false
        }
        binding.mealBtn2.setOnClickListener {
            binding.mealBtn2.isSelected = true
            binding.mealBtn1.isSelected = false
            binding.mealBtn3.isSelected = false
        }
        binding.mealBtn3.setOnClickListener {
            binding.mealBtn3.isSelected = true
            binding.mealBtn1.isSelected = false
            binding.mealBtn2.isSelected = false
        }

        // 배뇨 질문 버튼
        binding.peeGood.setOnClickListener {
            binding.peeGood.isSelected = true
            binding.peeBad.isSelected = false
        }
        binding.peeBad.setOnClickListener {
            binding.peeBad.isSelected = true
            binding.peeGood.isSelected = false
        }

        // 배변 질문 버튼
        binding.pooGood.setOnClickListener {
            binding.pooGood.isSelected = true
            binding.pooBad.isSelected = false
        }
        binding.pooBad.setOnClickListener {
            binding.pooBad.isSelected = true
            binding.pooGood.isSelected = false
        }

        // 작성 완료 버튼
        binding.diarySaveBtn.setOnClickListener {
            // 토스트 메시지 띄워주고 건강 화면으로 이동해 주기
            // 나중에는 데이터 모델로 만들어서 서버에 보내 주는 것 구현해야 함
            Toast.makeText(context, "저장되었습니다!", Toast.LENGTH_SHORT).show()
            (activity as NaviActivity).replace(HealthFragment())
        }

        // 키보드 올라 왔을 때 화면 가리는 것 방지하는 코드
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
        onShowKeyboard = { keyboardHeight ->
            binding.diarySv.run {
                smoothScrollTo(scrollX, scrollY + keyboardHeight)
            }
        })

        return binding?.root
    }
}