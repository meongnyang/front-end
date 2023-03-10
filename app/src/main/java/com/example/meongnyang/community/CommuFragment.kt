package com.example.meongnyang.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.databinding.CommuFragmentMainBinding
import com.example.meongnyang.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommuFragment : Fragment() {
    // dataBinding
    private lateinit var binding: CommuFragmentMainBinding

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

        postList = arrayListOf()

        // 기본값: 모든 카테고리 보이게
        binding.allBtn.isSelected = true

        binding.postView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.postView.adapter = postListAdapter
        showAllPost()


        // 리스트 아이템 클릭 시 해당 글 자세히 보기
        postListAdapter.setItemClickListener(object : PostListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val retrofit = RetrofitApi.create()
                var title = Title(listItems[position].title)
                retrofit.findPostId(title).enqueue(object : Callback<PostId> {
                    override fun onFailure(call: Call<PostId>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<PostId>, response: Response<PostId>) {
                        val postId = response.body()!!.postId
                        val intent = Intent(context, CommentActivity::class.java)
                        intent.putExtra("postId", postId)
                        startActivity(intent)
                    }
                })
            }
        })

        // 카테고리 선택
        binding.allBtn.setOnClickListener {
            binding.allBtn.isSelected = true
            binding.freeBtn.isSelected = false
            binding.askBtn.isSelected = false
            binding.boastBtn.isSelected = false
            showAllPost()
        }

        binding.freeBtn.setOnClickListener {
            binding.freeBtn.isSelected = true
            binding.askBtn.isSelected = false
            binding.boastBtn.isSelected = false
            binding.allBtn.isSelected = false
            // 포스트에서 카테고리가 "자유"에 해당하는 것만 골라서 recyclerView 갱신하도록 하기
            showPost("자유")
        }
        binding.askBtn.setOnClickListener {
            binding.askBtn.isSelected = true
            binding.freeBtn.isSelected = false
            binding.boastBtn.isSelected = false
            binding.allBtn.isSelected = false
            showPost("질문")
        }
        binding.boastBtn.setOnClickListener {
            binding.boastBtn.isSelected = true
            binding.freeBtn.isSelected = false
            binding.askBtn.isSelected = false
            binding.allBtn.isSelected = false
            showPost("1일 1자랑")
        }

        binding.writeBtn.setOnClickListener {
            (activity as NaviActivity).replace(WriteFragment())
        }

        return binding.root
    }

    // 서버와 통신
    private fun showAllPost() {
        retrofit.findPosts().enqueue(object: Callback<List<GetPosts>> {
            override fun onResponse(call: Call<List<GetPosts>>, response: Response<List<GetPosts>>) {
                if (response.isSuccessful) {
                    postList.clear()
                    postList.addAll(response.body()!!)
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
    }

    private fun showPost(category: String) {
        retrofit.findPosts().enqueue(object: Callback<List<GetPosts>> {
            override fun onResponse(call: Call<List<GetPosts>>, response: Response<List<GetPosts>>) {
                if (response.isSuccessful) {
                    postList.clear()
                    postList.addAll(response.body()!!)
                    listItems.clear()
                    for (document in response.body()!!) {
                        if (document.categoryName == category) {
                            val item = Post(document.title,
                                document.categoryName)
                            listItems.add(item)
                        }
                    }
                    postListAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<GetPosts>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }

}
