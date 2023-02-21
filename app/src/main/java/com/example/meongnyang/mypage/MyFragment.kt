package com.example.meongnyang.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.databinding.MypageFragmentMyBinding
import com.example.meongnyang.login.TypeActivity
import com.example.meongnyang.model.Id
import com.example.meongnyang.model.User
import com.example.meongnyang.model.allPet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFragment : Fragment() {
    private lateinit var binding: MypageFragmentMyBinding
    private val model: MypageViewModel by activityViewModels()

    val retrofit = RetrofitApi.create()
    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

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

        model.nameData.observe(viewLifecycleOwner, Observer {
            binding.userName.text = it.toString()
        })

        val bundle = arguments
        if (bundle?.getString("nickname") != null) {
            var newName = bundle.getString("nickname")
            model.updateNickname(newName!!)
        }
        if (bundle?.getString("userImg") != null) {
            var newImg = bundle.getString("userImg")
            model.updateUserImg(newImg!!)
        }

        binding.apply {
            mypage = model
            lifecycleOwner = this@MyFragment
        }

        // 내 정보 수정 화면으로 이동하기
        binding.modifyProfile.setOnClickListener {
            (activity as NaviActivity).replace(ModifyFragment())
        }
        binding.userView.setOnClickListener {
            (activity as NaviActivity).replace(ModifyFragment())
        }
        // 반려동물 추가하기
        binding.addPet.setOnClickListener {
            val intent = Intent(context, TypeActivity::class.java)
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