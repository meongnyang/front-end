package com.example.meongnyang.home

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.meongnyang.DB.DBManager
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.community.CommuFragment
import com.example.meongnyang.databinding.FragmentHomeBinding
import com.example.meongnyang.diary.DiaryFragment
import com.example.meongnyang.feed.FeedFragment
import com.example.meongnyang.qna.QnaFragment
import com.example.meongnyang.map.PlayMapActivity
import com.example.meongnyang.model.PetModel
import com.example.meongnyang.skin.SkinMainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dbManager: DBManager
    private lateinit var database: SQLiteDatabase

    var memberId = 0
    var conimalId = 0
    var name = ""
    var strType = ""
    var days = ""

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        /*dbManager = DBManager(context, "personnel", null, 1)
        database = dbManager.readableDatabase
        var cursor: Cursor

        cursor = database.rawQuery("SELECT * FROM personnel", null)

        while (cursor.moveToNext()) {
            memberId = cursor.getInt((cursor.getColumnIndex("memberId")))
            conimalId = cursor.getInt(cursor.getColumnIndex("conimalId"))
        }

        Log.d("id", memberId.toString())
        Log.d("id", conimalId.toString())

        cursor.close()
        database.close()*/

        //memberId = arguments?.getInt("memberId")!!
        //conimalId = arguments?.getInt("conimalId")!!

        val retrofit = RetrofitApi.create()

        // GET으로 정보 얻고 텍스트 갱신
        retrofit.getPet(5).enqueue(object: Callback<PetModel> {
            override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                Log.d("pet", response.body().toString())
                name = response.body()!!.name
                strType = strType(response.body()!!.type)
                days = response.body()!!.ddayadopt.toString()
                /*Log.d("pet", name)
                Log.d("pet", strType)
                Log.d("pet", days)*/
                binding.titleMsg.text = "오늘도 ${name}의\n건강한 ${strType}생을 위해! 🙌"
                binding.days.text = days
            }

            override fun onFailure(call: Call<PetModel>, t: Throwable) {
                Log.d("Post", t.toString())
            }
        })

        /*binding.apply {
            lifecycleOwner = this@HomeFragment
        }*/

        // 메뉴 클릭 이벤트
        binding.FeedMenu.setOnClickListener {
            (activity as NaviActivity).replaceFragment(FeedFragment())
        }
        binding.skinMenu.setOnClickListener {
            val intent = Intent(context, SkinMainActivity::class.java)
            startActivity(intent)
        }
        binding.playMapMenu.setOnClickListener {
            val intent = Intent(context, PlayMapActivity::class.java)
            startActivity(intent)
        }
        binding.hospitalMenu.setOnClickListener {
            val intent = Intent(context, com.example.meongnyang.map.MapActivity::class.java)
            startActivity(intent)
        }
        binding.qnaMenu.setOnClickListener {
            (activity as NaviActivity).replaceFragment(QnaFragment())
        }
        binding.diaryMenu.setOnClickListener {
            (activity as NaviActivity).replaceFragment(DiaryFragment())
        }
        binding.commuMenu.setOnClickListener {
            (activity as NaviActivity).replaceFragment(CommuFragment())
        }

        return binding?.root
    }
    private fun strType(type: Int): String {
        var strType = ""
        strType = if (type == 1) {
            "견"
        } else "묘"

        return strType
    }
}