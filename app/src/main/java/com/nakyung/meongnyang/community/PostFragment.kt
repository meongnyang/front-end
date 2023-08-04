package com.nakyung.meongnyang.community

import android.app.AlertDialog
import android.content.Intent
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
import com.nakyung.meongnyang.NaviActivity
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.databinding.CommuFragmentPostBinding
import com.nakyung.meongnyang.model.*
import com.nakyung.meongnyang.mypage.KeyboardVisibilityUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.nakyung.meongnyang.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostFragment : Fragment() {
    private lateinit var binding: CommuFragmentPostBinding
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    private lateinit var viewModel: PostViewModel
    private lateinit var viewModelFactory: PostViewModelFactory

    private lateinit var commentList: ArrayList<Comment> // 댓글들 담을 배열
    private val listItems = arrayListOf<AllComment>() // 리사이클러뷰 아이템
    private val commentListAdapter = CommentListAdpater(listItems) // adapter

    var postId = 0
    var memberId = App.prefs.getInt("memberId", 0)

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
        Log.d("comments: postId", postId.toString())

        commentList = arrayListOf()

        viewModelFactory = PostViewModelFactory(postId)
        viewModel = ViewModelProvider(this@PostFragment,
            viewModelFactory).get(PostViewModel::class.java)

        // 내가 쓴 글인 경우 수정 | 삭제 보이도록 하기
        retrofit.getPost(postId).enqueue(object : Callback<GetPosts> {
            override fun onFailure(call: Call<GetPosts>, t: Throwable) {

            }

            override fun onResponse(call: Call<GetPosts>, response: Response<GetPosts>) {
                if (memberId == response.body()!!.memberId) {
                    binding.postEdit.visibility =  View.VISIBLE
                    binding.postEdit2.visibility = View.VISIBLE
                    binding.postDelete.visibility = View.VISIBLE
                }
            }
        })

        binding.postEdit.setOnClickListener {
            // 게시물 작성 화면으로 이동, postId 담아서 보내기
            val intent = Intent(context, WriteActivity::class.java)
            intent.putExtra("postId", postId)
            startActivity(intent)
        }

        // 삭제 버튼 눌렀을 때 dialog 띄우기
        binding.postDelete.setOnClickListener {
            showDialog()
            //val dialog = CommuDialog(requireContext())
            //dialog.delShow(postId)
        }


        // 댓글 달기
        if (bundle.getString("contents") != null) {
            var comment = bundle!!.getString("contents")
            writeComment(postId, comment!!)
        } else showComment(postId)

        // 대댓글 달기
        if (bundle.getString("reContents") != null) {
            var comment = bundle.getString("reContents")
            var commentId = bundle.getInt("commentId")
            writeReComment(commentId, postId, comment!!)
        } else showComment(postId)

        binding.likeBtn.setOnClickListener {
            updateCount(postId)
            Toast.makeText(context, "좋아요 완료!", Toast.LENGTH_SHORT).show()
        }

        // 댓글 클릭했을 때 대댓글 작성하는 화면으로
        commentListAdapter.setItemClickListener(object : CommentListAdpater.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                var contents = listItems[position].contents
                Log.d("comments", contents)
                retrofit.getCommentId(Contents(contents)).enqueue(object : Callback<CommentId> {
                    override fun onFailure(call: Call<CommentId>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<CommentId>, response: Response<CommentId>) {
                        var commentId = response.body()!!.commentId
                        Log.d("comments", commentId.toString())
                        val intent = Intent(context, RecommentActivity::class.java)
                        intent.putExtra("commentId", commentId)
                        intent.putExtra("postId", postId)
                        Log.d("comments", postId.toString())
                        startActivity(intent)
                    }
                })
            }
        })

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
                    for (document in commentList) {
                        val item = AllComment(document.nickname, document.contents, document.commentList)
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
        retrofit.writeComment(memberId, postId, comment).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                Toast.makeText(context, "댓글 작성 완료!", Toast.LENGTH_SHORT).show()
                showComment(postId)
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Toast.makeText(context, "작성 실패, 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun writeReComment(commentId: Int, postId: Int, comment: String) {
        retrofit.reWriteComment(memberId, commentId, postId, comment).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                Toast.makeText(context, "댓글 작성 완료!", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra("postId", postId)
                startActivity(intent)
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Toast.makeText(context, "작성 실패, 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateCount(postId: Int) {
        retrofit.updateLikes(memberId, postId).enqueue(object : Callback<Count> {
            override fun onResponse(call: Call<Count>, response: Response<Count>) {
                (activity as CommentActivity).sendId(postId)
            }

            override fun onFailure(call: Call<Count>, t: Throwable) {

            }
        })
    }

    fun deletePost(postId: Int) {
        retrofit.deletePost(postId).enqueue(object : Callback<okhttp3.ResponseBody> {
            override fun onFailure(call: Call<okhttp3.ResponseBody>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<okhttp3.ResponseBody>,
                response: Response<okhttp3.ResponseBody>
            ) {
                val intent = Intent(context, NaviActivity::class.java)
                intent.putExtra("fragment", "community")
                startActivity(intent)
            }
        })
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(context)
        builder
            .setTitle("삭제")
            .setMessage("게시글을 삭제하시겠어요?")
            .setIcon(R.drawable.app_icon)
            .setPositiveButton("삭제") { dialog, which ->
                deletePost(postId)
            }
            .setNegativeButton("취소") { dialog, which ->

            }
            .create()
            .show()
    }
}