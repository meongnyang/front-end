package com.example.meongnyang.community

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.databinding.CommuFragmentPostBinding
import com.example.meongnyang.model.*
import com.example.meongnyang.mypage.KeyboardVisibilityUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostFragment : Fragment() {
    private lateinit var binding: CommuFragmentPostBinding
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    private lateinit var viewModel: PostViewModel
    private lateinit var viewModelFactory: PostViewModelFactory

    private lateinit var commentList: ArrayList<Comment> // 댓글들 담을 배열
    private val listItems = arrayListOf<CommentModel>() // 리사이클러뷰 아이템
    private val commentListAdapter = CommentListAdpater(listItems) // adapter

    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

    var postId = 0
    var memberId = 0

    val retrofit = RetrofitApi.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.commu_fragment_post,
            container,
            false
        )

        val bundle = arguments
        postId = bundle!!.getInt("postId")

        commentList = arrayListOf()

        viewModelFactory = PostViewModelFactory(postId)
        viewModel = ViewModelProvider(this@PostFragment,
            viewModelFactory).get(PostViewModel::class.java)


        if (bundle!!.getString("contents") != null) {
            var comment = bundle!!.getString("contents")
            writeComment(postId, comment!!)
        } else showComment(postId)

        binding.likeBtn.setOnClickListener {
            updateCount(postId)
            Toast.makeText(context, "좋아요 완료!", Toast.LENGTH_SHORT).show()
        }


        binding.apply {
            post = viewModel
            lifecycleOwner = this@PostFragment
        }


        // 키보드 올라 왔을 때 화면 가리는 것 방지하는 코드
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = { keyboardHeight ->
                binding.postSv.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })

        return binding.root
    }

    // 댓글 보기 함수
    private fun showComment(postId: Int) {
        binding.commentView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.commentView.adapter = commentListAdapter

        retrofit.getComments(postId).enqueue(object: Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    commentList.clear()
                    commentList.addAll(response.body()!!)
                    listItems.clear()
                    for (document in response.body()!!) {
                        val item = CommentModel(document.nickname,
                        document.contents)
                        listItems.add(item)
                    }
                    commentListAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }
    private fun writeComment(postId: Int, comment: String) {
        fbFirestore!!.collection("users").document(uid).get()
            .addOnSuccessListener { documentsSnapshot ->
                var id = documentsSnapshot.toObject<Id>()!!

                retrofit.writeComment(id.memberId!!, postId, comment).enqueue(object : Callback<Comment> {
                    override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                        Toast.makeText(context, "댓글 작성 완료!", Toast.LENGTH_SHORT).show()
                        showComment(postId)
                    }

                    override fun onFailure(call: Call<Comment>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
    }

    private fun updateCount(postId: Int) {
        fbFirestore!!.collection("users").document(uid).get()
            .addOnSuccessListener { documentsSnapshot ->
                var id = documentsSnapshot.toObject<Id>()!!
                retrofit.updateLikes(id.memberId!!, postId).enqueue(object : Callback<Count> {
                    override fun onResponse(call: Call<Count>, response: Response<Count>) {
                        (activity as PostActivity).sendId(postId)
                    }

                    override fun onFailure(call: Call<Count>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
    }
}