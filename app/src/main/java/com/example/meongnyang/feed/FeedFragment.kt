package com.example.meongnyang.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.community.CommentListAdpater
import com.example.meongnyang.databinding.FragmentFeedBinding
import com.example.meongnyang.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    private lateinit var feedList: ArrayList<Feed> // 사료들 담을 배열
    private val listItems = arrayListOf<FeedModel>() // 리사이클러뷰 아이템
    private val feedListAdapter = FeedListAdapter(listItems) // adapter

    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

    val retrofit = RetrofitApi.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_feed,
            container,
            false
        )

        feedList = arrayListOf() // 사료 정보 담는 배열

        // typeId 받기
        fbFirestore!!.collection("users").document(uid).get()
            .addOnSuccessListener { documentsSnapshot ->
                var id = documentsSnapshot.toObject<Id>()!!

                retrofit.getPet(id.conimalId!!).enqueue(object : Callback<PetModel> {
                    override fun onFailure(call: Call<PetModel>, t: Throwable) {
                        Log.d("fail", "정보 불러오기 실패")
                    }

                    override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                        var typeId = response.body()!!.type

                        showFeed(typeId)
                    }
                })
            }

        return binding.root
    }

    // 사료 정보 보기 함수
    private fun showFeed(typeId: Int) {
        binding.feedRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.feedRecyclerView.adapter = feedListAdapter

        retrofit.getFeed(typeId).enqueue(object : Callback<List<Feed>> {
            override fun onFailure(call: Call<List<Feed>>, t: Throwable) {
                Log.d("fail", "사료 정보 불러오기 실패")
            }

            override fun onResponse(call: Call<List<Feed>>, response: Response<List<Feed>>) {
                Log.d("feed", response.body()!!.toString())
                feedList.clear()
                feedList.addAll(response.body()!!)

                listItems.clear()
                for (document in response.body()!!) {
                    val item = FeedModel(document.feedId, document.name, document.img, document.efficacy)
                    listItems.add(item)
                }
                feedListAdapter.notifyDataSetChanged()
            }
        })
    }
}