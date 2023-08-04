package com.nakyung.meongnyang.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nakyung.meongnyang.App
import com.nakyung.meongnyang.NaviActivity
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.community.CommentActivity
import com.nakyung.meongnyang.community.PostListAdapter
import com.nakyung.meongnyang.databinding.CommuActivityCommentBinding
import com.nakyung.meongnyang.databinding.LoginSelectPetActivityBinding
import com.nakyung.meongnyang.model.*
import kotlinx.coroutines.selects.select
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectPetActivity : AppCompatActivity() {
    private lateinit var binding: LoginSelectPetActivityBinding

    private lateinit var petList: ArrayList<PetModel> // 게시물들 담을 배열
    private val listItems = arrayListOf<PetList>() // 리사이클러뷰 아이템
    private val selectListAdapter = SelectListAdapter(listItems) // adapter

    val retrofit = RetrofitApi.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginSelectPetActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        petList = arrayListOf()

        binding.petListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.petListView.adapter = selectListAdapter

        // 모든 반려동물 정보 불러오기
        var memberId = App.prefs.getInt("memberId", 0)
        retrofit.getAllPet(memberId).enqueue(object : Callback<List<PetModel>> {
            override fun onFailure(call: Call<List<PetModel>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<PetModel>>,
                response: Response<List<PetModel>>
            ) {
                if (response.isSuccessful) {
                    petList.clear()
                    petList.addAll(response.body()!!)

                    Log.d("reponse", response.body()!!.toString())
                    listItems.clear()
                    for (document in response.body()!!) {
                        val item = PetList(document.name, document.birth, document.speciesName, document.conimalId)
                        listItems.add(item)
                    }
                    selectListAdapter.notifyDataSetChanged()
                }
            }
        })

        // 홈으로 가기
        selectListAdapter.setItemClickListener(object : SelectListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                var conimalId = listItems[position].conimalId
                App.prefs.setInt("conimalId", conimalId)
                var intent = Intent(this@SelectPetActivity, NaviActivity::class.java)
                startActivity(intent)
            }
        })
    }
}