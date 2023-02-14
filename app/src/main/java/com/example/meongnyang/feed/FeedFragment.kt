package com.example.meongnyang.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.meongnyang.Dialog.FeedDialog
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.databinding.FeedFragmentMainBinding
import com.example.meongnyang.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class FeedFragment : Fragment() {
    private lateinit var binding: FeedFragmentMainBinding
    private lateinit var feedList: ArrayList<Feed> // 사료들 담을 배열
    private val listItems = arrayListOf<FeedModel>() // 리사이클러뷰 아이템
    private val feedListAdapter = FeedListAdapter(listItems) // adapter

    var fbAuth = FirebaseAuth.getInstance()
    var fbFirestore = FirebaseFirestore.getInstance()
    val uid = fbAuth.uid.toString()

    val retrofit = RetrofitApi.create()

    var typeId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.feed_fragment_main,
            container,
            false
        )

        binding.feedRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.feedRecyclerView.adapter = feedListAdapter

        binding.allBtn.isSelected = true
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
                        typeId = response.body()!!.type
                        showFeed(typeId)
                    }
                })
            }

        // 눌렀을 때 상세 정보 띄우기
        feedListAdapter.setItemClickListener(object: FeedListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val dialog = FeedDialog(context!!)
                dialog.show(listItems[position].name, listItems[position].material, listItems[position].ingredient)
            }
        })

        binding.boneBtn.setOnClickListener {
            binding.boneBtn.isSelected = true
            binding.allBtn.isSelected = false
            binding.hairBtn.isSelected = false
            binding.dietBtn.isSelected = false
            binding.diseaseBtn.isSelected = false
            binding.oldBtn.isSelected = false
            binding.freshBtn.isSelected = false

            feedByEfficacy(1)
        }
        binding.allBtn.setOnClickListener {
            binding.boneBtn.isSelected = false
            binding.allBtn.isSelected = true
            binding.hairBtn.isSelected = false
            binding.dietBtn.isSelected = false
            binding.diseaseBtn.isSelected = false
            binding.oldBtn.isSelected = false
            binding.freshBtn.isSelected = false

            showFeed(typeId)
        }
        binding.hairBtn.setOnClickListener {
            binding.boneBtn.isSelected = false
            binding.allBtn.isSelected = false
            binding.hairBtn.isSelected = true
            binding.dietBtn.isSelected = false
            binding.diseaseBtn.isSelected = false
            binding.oldBtn.isSelected = false
            binding.freshBtn.isSelected = false

            feedByEfficacy(4)
        }
        binding.dietBtn.setOnClickListener {
            binding.boneBtn.isSelected = false
            binding.allBtn.isSelected = false
            binding.hairBtn.isSelected = false
            binding.dietBtn.isSelected = true
            binding.diseaseBtn.isSelected = false
            binding.oldBtn.isSelected = false
            binding.freshBtn.isSelected = false

            feedByEfficacy(3)
        }
        binding.diseaseBtn.setOnClickListener {
            binding.boneBtn.isSelected = false
            binding.allBtn.isSelected = false
            binding.hairBtn.isSelected = false
            binding.dietBtn.isSelected = false
            binding.diseaseBtn.isSelected = true
            binding.oldBtn.isSelected = false
            binding.freshBtn.isSelected = false

            feedByEfficacy(5)
        }
        binding.oldBtn.setOnClickListener {
            binding.boneBtn.isSelected = false
            binding.allBtn.isSelected = false
            binding.hairBtn.isSelected = false
            binding.dietBtn.isSelected = false
            binding.diseaseBtn.isSelected = false
            binding.oldBtn.isSelected = true
            binding.freshBtn.isSelected = false

            feedByEfficacy(6)
        }
        binding.freshBtn.setOnClickListener {
            binding.boneBtn.isSelected = false
            binding.allBtn.isSelected = false
            binding.hairBtn.isSelected = false
            binding.dietBtn.isSelected = false
            binding.diseaseBtn.isSelected = false
            binding.oldBtn.isSelected = false
            binding.freshBtn.isSelected = true

            feedByEfficacy(2)
        }

        return binding.root
    }

    // 사료 정보 보기 함수
    private fun showFeed(typeId: Int) {
        retrofit.getFeed(typeId).enqueue(object : Callback<List<Feed>> {
            override fun onFailure(call: Call<List<Feed>>, t: Throwable) {
                Log.d("fail", "사료 정보 불러오기 실패")
            }

            override fun onResponse(call: Call<List<Feed>>, response: Response<List<Feed>>) {
                feedList.clear()
                feedList.addAll(response.body()!!)
                listItems.clear()

                for (document in feedList) {
                    var efficacy = "👍 "
                    var count = 0
                    for (doc in document.efficacyList) {
                        efficacy += "${doc.name}"
                        count += 1

                        if (count < document.efficacyList.size) {
                            efficacy += ", "
                        }
                    }
                    val item = FeedModel(document.feedId, document.name, document.img, efficacy, document.ingredient, document.material)
                    listItems.add(item)
                }
                feedListAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun feedByEfficacy(efficacyId: Int) {
        retrofit.getByEfficacy(efficacyId).enqueue(object : Callback<List<Feed>> {
            override fun onFailure(call: Call<List<Feed>>, t: Throwable) {
                Log.d("fail", "사료 정보 불러오기 실패")
            }

            override fun onResponse(call: Call<List<Feed>>, response: Response<List<Feed>>) {
                feedList.clear()
                listItems.clear()
                feedList.addAll(response.body()!!)

                for (document in feedList) {
                    var efficacy = "👍 "
                    var count = 0
                    for (doc in document.efficacyList) {
                        efficacy += "${doc.name}"
                        count += 1

                        if (count < document.efficacyList.size) {
                            efficacy += ", "
                        }
                    }
                    val item = FeedModel(document.feedId, document.name, document.img, efficacy, document.ingredient, document.material)
                    listItems.add(item)
                }
                feedListAdapter.notifyDataSetChanged()
            }
        })
    }
}