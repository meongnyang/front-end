package com.example.meongnyang.community

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.databinding.CommuFragmentMainBinding
import com.example.meongnyang.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommuFragment : Fragment() {
    // dataBinding, ViewModel
    private lateinit var binding: CommuFragmentMainBinding
    private val model: CommuViewModel by activityViewModels()

    private lateinit var postList: ArrayList<GetPosts> // 게시물들 담을 배열
    private val listItems = arrayListOf<Post>() // 리사이클러뷰 아이템
    private val postListAdapter = PostListAdapter(listItems) // adapter

    val retrofit = RetrofitApi.create()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.commu_fragment_main,
            container,
            false
        )
        binding.apply {
            posts = model
            lifecycleOwner = this@CommuFragment
        }

        postList = arrayListOf()

        // 서버에서 게시글 받아오기
        retrofit.getAllPosts().enqueue(object: Callback<List<GetPosts>> {
            override fun onResponse(call: Call<List<GetPosts>>, response: Response<List<GetPosts>>) {
                if (response.isSuccessful) {
                    postList.clear()
                    postList.addAll(response.body()!!)

                    Log.d("post", postList.toString())

                    listItems.clear()
                    for (document in response.body()!!) {
                        val item = Post(document.title,
                            document.categoryName)
                        listItems.add(item)
                    }
                    postListAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<GetPosts>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })

        binding.postView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.postView.adapter = postListAdapter


        // 리스트 아이템 클릭 시 해당 글 자세히 보기
        postListAdapter.setItemClickListener(object : PostListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // 여기에 구현
            }
        })

        // 카테고리 선택
        binding.allBtn.setOnClickListener {
            binding.allBtn.isSelected = true
            binding.freeBtn.isSelected = false
            binding.askBtn.isSelected = false
            binding.boastBtn.isSelected = false
        }

        binding.freeBtn.setOnClickListener {
            binding.freeBtn.isSelected = true
            binding.askBtn.isSelected = false
            binding.boastBtn.isSelected = false
            binding.allBtn.isSelected = false
            // 포스트에서 카테고리가 "자유"에 해당하는 것만 골라서 recyclerView 갱신하도록 하기
        }
        binding.askBtn.setOnClickListener {
            binding.askBtn.isSelected = true
            binding.freeBtn.isSelected = false
            binding.boastBtn.isSelected = false
            binding.allBtn.isSelected = false
        }
        binding.boastBtn.setOnClickListener {
            binding.boastBtn.isSelected = true
            binding.freeBtn.isSelected = false
            binding.askBtn.isSelected = false
            binding.allBtn.isSelected = false
        }

        return binding.root
    }
}
