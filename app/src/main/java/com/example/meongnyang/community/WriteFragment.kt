package com.example.meongnyang.community

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.databinding.CommuFragmentWriteBinding
import com.example.meongnyang.model.GetPosts
import com.example.meongnyang.model.Id
import com.example.meongnyang.model.PetModel
import com.example.meongnyang.model.PostModel
import com.example.meongnyang.mypage.KeyboardVisibilityUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteFragment : Fragment() {
    private lateinit var binding: CommuFragmentWriteBinding
    var type = 0

    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.commu_fragment_write,
            container,
            false
        )

        val retrofit = RetrofitApi.create() // 서버와 통신 연결

        // 스피너 설정
        val categoryAdapter =
            context?.let {
                ArrayAdapter.createFromResource(it, R.array.category_array, android.R.layout.simple_spinner_dropdown_item)
            }
        categoryAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selectCategory.adapter = categoryAdapter // 어댑터와 연결

        // 작성 완료 버튼 누르면 입력한 제목, 내용, 카테고리, 사진 (있으면 링크, 없으면 값?) 넘겨주기
        binding.finishBtn.setOnClickListener {
            var category = binding.selectCategory.selectedItemPosition
            var title = binding.postTitle.editText?.text.toString()
            var contents = binding.postContent.editText?.text.toString()
            var img = "" // 일단 이미지는 나중에... ㅠㅠ

            fbFirestore!!.collection("users").document(uid).get()
                .addOnSuccessListener { documentsSnapshot ->
                    var id = documentsSnapshot.toObject<Id>()!!

                    retrofit.getPet(id.conimalId!!).enqueue(object: Callback<PetModel> {
                        override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                            type = response.body()!!.type
                        }

                        override fun onFailure(call: Call<PetModel>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })

                    val data = PostModel(category, type, title, contents, img)

                    retrofit.createPost(data, id.conimalId!!).enqueue(object: Callback<GetPosts> {
                        override fun onResponse(call: Call<GetPosts>, response: Response<GetPosts>) {
                            if (!response.body().toString().isEmpty()) {
                                Log.d("result", response.body().toString())
                            }
                        }
                        override fun onFailure(call: Call<GetPosts>, t: Throwable) {
                            Log.d("error", "fail")
                            Log.d("error", t.message.toString())
                        }
                    })

                    (activity as NaviActivity).replace(CommuFragment())
                }

        }

        return binding.root
    }
}
