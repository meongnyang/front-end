package com.nakyung.meongnyang.health

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.nakyung.meongnyang.NaviActivity
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.databinding.FragmentHealthBinding
import com.nakyung.meongnyang.diary.DiaryFragment
import com.nakyung.meongnyang.diary.DiaryViewModel
import com.nakyung.meongnyang.diary.DiaryViewModelFactory
import com.nakyung.meongnyang.eye.EyeMainActivity
import com.nakyung.meongnyang.feed.FeedFragment
import com.nakyung.meongnyang.model.Id
import com.nakyung.meongnyang.qna.QnaFragment
import com.nakyung.meongnyang.skin.SkinMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class HealthFragment : Fragment() {
    private lateinit var binding: FragmentHealthBinding
    private lateinit var viewModel: DiaryViewModel
    private lateinit var viewModelFactory: DiaryViewModelFactory

    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

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

        fbFirestore!!.collection("users").document(uid).get()
            .addOnSuccessListener { documentsSnapshot ->
                var id = documentsSnapshot.toObject<Id>()!!
                if (id.recordId != 0) {
                    binding.clickText.visibility = View.GONE
                    binding.noDataText.visibility = View.GONE
                    binding.mealText.visibility = View.VISIBLE
                    binding.voidText.visibility = View.VISIBLE
                    binding.excText.visibility = View.VISIBLE

                    viewModelFactory = DiaryViewModelFactory(id.recordId!!)
                    viewModel = ViewModelProvider(this@HealthFragment,
                        viewModelFactory).get(DiaryViewModel::class.java)

                    binding.apply {
                        diary = viewModel
                        lifecycleOwner = this@HealthFragment
                    }
                } else {
                    binding.clickText.visibility = View.VISIBLE
                    binding.noDataText.visibility = View.VISIBLE
                    binding.mealText.visibility = View.GONE
                    binding.voidText.visibility = View.GONE
                    binding.excText.visibility = View.GONE
                }
            }

        // 메뉴 화면 이동
        binding.skinCheckBtn.setOnClickListener {
            val intent = Intent(context, SkinMainActivity::class.java)
            startActivity(intent)
        }
        binding.eyeCheckBtn.setOnClickListener {
            val intent = Intent(context, EyeMainActivity::class.java)
            startActivity(intent)
        }
        binding.qnaBtn.setOnClickListener {
            (activity as NaviActivity).replace(QnaFragment())
        }
        binding.hospitalBtn.setOnClickListener {
            val intent = Intent(context, com.nakyung.meongnyang.map.MapActivity::class.java)
            startActivity(intent)
        }
        binding.feedBtn.setOnClickListener {
            (activity as NaviActivity).replace(FeedFragment())
        }

        // 건강기록부 작성 화면으로
        binding.diaryView.setOnClickListener {
            (activity as NaviActivity).replace(DiaryFragment())
        }

        return binding?.root
    }

}