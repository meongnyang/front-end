package com.nakyung.meongnyang.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nakyung.meongnyang.NaviActivity
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.databinding.MypageFragmentMyBinding
import com.nakyung.meongnyang.login.TypeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.nakyung.meongnyang.model.Id
import org.checkerframework.checker.units.qual.Length
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

        model.userData.observe(viewLifecycleOwner, Observer {
            Glide.with(binding.userProfile.context)
                .load(it.toString())
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.userProfile)
        })

        val bundle = arguments
        if (bundle?.getString("nickname") != null) {
            var newName = bundle.getString("nickname")
            model.updateNickname(newName!!)
        }
        if (bundle?.getString("userImg") != null) {
            var newImg = bundle.getString("userImg")
            Log.d("profile", newImg.toString())
            Glide.with(binding.userProfile.context)
                .load(newImg)
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.userProfile)
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
            intent.putExtra("addPet", true)
            startActivity(intent)
        }
        // 메인 반려동물 변경
        binding.changePet.setOnClickListener {
            val intent = Intent(context, ChoiceActivity::class.java)
            startActivity(intent)
        }

        // 회원 탈퇴
        binding.deleteMember.setOnClickListener {
            // memberId 가져오기
            fbFirestore.collection("users").document(uid).get()
                .addOnSuccessListener { documentsSnapshot ->
                    var id = documentsSnapshot.toObject<Id>()!!
                    var memberId = id.memberId!!

                    retrofit.deleteProfile(memberId).enqueue(object : Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(context, "회원 탈퇴 실패, 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            Log.d("member", "회원 탈퇴 성공")
                        }
                    })
                }
        }

        return binding?.root
    }
}