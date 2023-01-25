package com.example.meongnyang.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.meongnyang.R
import com.example.meongnyang.databinding.CommuFragmentPostBinding
import com.example.meongnyang.mypage.KeyboardVisibilityUtils

class PostFragment : Fragment() {
    private lateinit var binding: CommuFragmentPostBinding
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    private lateinit var viewModel: PostViewModel
    private lateinit var viewModelFactory: PostViewModelFactory
    var postId = 0
    var memberId = 0

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
        //val retrofit = RetrofitApi.create()

        val bundle = arguments
        postId = bundle!!.getInt("postId")

        viewModelFactory = PostViewModelFactory(postId)
        viewModel = ViewModelProvider(this@PostFragment,
            viewModelFactory).get(PostViewModel::class.java)

        binding.apply {
            post = viewModel
            lifecycleOwner = this@PostFragment
        }

            /*retrofit.getPost(postId).enqueue(object : Callback<GetPosts> {
                override fun onResponse(call: Call<GetPosts>, response: Response<GetPosts>) {
                    memberId = response.body()!!.memberId
                    binding.viewTitle.text = response.body()!!.title
                    binding.viewContent.text = response.body()!!.contents
                    binding.postDate.text = response.body()!!.date
                    binding.likeCount.text = response.body()!!.count.toString()

                    retrofit.getMember(memberId).enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            binding.writer.text = response.body()!!.nickname
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }

                override fun onFailure(call: Call<GetPosts>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })*/

        // 키보드 올라 왔을 때 화면 가리는 것 방지하는 코드
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = { keyboardHeight ->
                binding.commuSV.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })

        return binding.root
    }
}