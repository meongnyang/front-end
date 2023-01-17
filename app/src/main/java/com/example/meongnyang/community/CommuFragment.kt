package com.example.meongnyang.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.meongnyang.R
import com.example.meongnyang.databinding.CommuFragmentMainBinding
import com.example.meongnyang.map.MapListAdapter
import com.example.meongnyang.model.MapList
import com.example.meongnyang.model.Post

class CommuFragment : Fragment() {
    // dataBinding, ViewModel
    private lateinit var binding: CommuFragmentMainBinding
    private val model: CommuViewModel by activityViewModels()

    private lateinit var postList: ArrayList<Post> // 게시물들 담을 배열
    private val listItems = arrayListOf<Post>() // 리사이클러뷰 아이템
    private val postListAdapter = PostListAdapter(listItems) // adapter

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

        postList = arrayListOf()

        return binding.root
    }
}