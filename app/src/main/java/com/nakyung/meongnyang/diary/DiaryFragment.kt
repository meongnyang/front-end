package com.nakyung.meongnyang.diary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.nakyung.meongnyang.NaviActivity
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.databinding.DiaryFragmentWriteBinding
import com.nakyung.meongnyang.health.HealthFragment
import com.nakyung.meongnyang.model.DiaryModel
import com.nakyung.meongnyang.model.Id
import com.nakyung.meongnyang.model.PostDiary
import com.nakyung.meongnyang.mypage.KeyboardVisibilityUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.nakyung.meongnyang.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryFragment : Fragment() {
    private lateinit var binding: DiaryFragmentWriteBinding
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    val retrofit = RetrofitApi.create()

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

        var meal = 0
        var voiding = 0
        var excretion = 0

        // 식이 질문 버튼
        binding.mealBtn1.setOnClickListener {
            binding.mealBtn1.isSelected = true
            binding.mealBtn2.isSelected = false
            binding.mealBtn3.isSelected = false
            meal = 1
        }
        binding.mealBtn2.setOnClickListener {
            binding.mealBtn2.isSelected = true
            binding.mealBtn1.isSelected = false
            binding.mealBtn3.isSelected = false
            meal = 2
        }
        binding.mealBtn3.setOnClickListener {
            binding.mealBtn3.isSelected = true
            binding.mealBtn1.isSelected = false
            binding.mealBtn2.isSelected = false
            meal = 3
        }

        // 배뇨 질문 버튼
        binding.peeGood.setOnClickListener {
            binding.peeGood.isSelected = true
            binding.peeBad.isSelected = false
            voiding = 1
        }
        binding.peeBad.setOnClickListener {
            binding.peeBad.isSelected = true
            binding.peeGood.isSelected = false
            voiding = 2
        }

        // 배변 질문 버튼
        binding.pooGood.setOnClickListener {
            binding.pooGood.isSelected = true
            binding.pooBad.isSelected = false
            excretion = 1
        }
        binding.pooBad.setOnClickListener {
            binding.pooBad.isSelected = true
            binding.pooGood.isSelected = false
            excretion = 2
        }

        // 작성 완료 버튼
        binding.diarySaveBtn.setOnClickListener {
            // 토스트 메시지 띄워주고 건강 화면으로 이동해 주기
            var memberId = App.prefs.getInt("memberId", 0)
            var conimalId = App.prefs.getInt("conimalId", 0)
            var diary = PostDiary(conimalId, memberId, meal, voiding, binding.peeReason.text.toString(), excretion, binding.pooReason.text.toString())
            Log.d("diary", diary.toString())

            retrofit.writeDiary(memberId, conimalId, diary).enqueue(object : Callback<DiaryModel> {
                override fun onResponse(
                    call: Call<DiaryModel>,
                    response: Response<DiaryModel>
                ) {
                    Log.d("response", response.body()!!.toString())
                    var diaryId = response.body()!!.recordId
                    Log.d("diaryId", diaryId.toString())
                    // diaryId 저장
                    App.prefs.setInt("diaryId", diaryId)
                    Toast.makeText(context, "오늘의 기록 완료 ✍️", Toast.LENGTH_SHORT).show()
                    sendId(diaryId)
                }

                override fun onFailure(call: Call<DiaryModel>, t: Throwable) {
                    Log.d("diary", t.message.toString())
                }
            })
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
    private fun sendId(recordId: Int) {
        val bundle = Bundle()
        bundle.putInt("recordId", recordId)
        //bundle.putBoolean("today", true)
        val healthFragment = HealthFragment()
        healthFragment.arguments = bundle
        (activity as NaviActivity).replace(healthFragment)
    }
}