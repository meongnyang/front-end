package com.example.meongnyang.qna

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.community.CommentListAdpater
import com.example.meongnyang.community.PostActivity
import com.example.meongnyang.community.PostFragment
import com.example.meongnyang.community.PostListAdapter
import com.example.meongnyang.databinding.QnaFragmentMainBinding
import com.example.meongnyang.model.*
import com.example.meongnyang.mypage.KeyboardVisibilityUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QnaFragment : Fragment() {
    private lateinit var binding: QnaFragmentMainBinding
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    private lateinit var qnaList: ArrayList<Qna> // 질문 담을 배열
    private val listItems = arrayListOf<String>() // 리사이클러뷰 아이템
    private val qnaListAdapter = QnaListAdapter(listItems) // adapter

    val retrofit = RetrofitApi.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.qna_fragment_main,
            container,
            false
        )

        qnaList = arrayListOf()

        showQuestion()

        // 키보드 올라 왔을 때 화면 가리는 것 방지하는 코드
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = { keyboardHeight ->
                binding.QnaSv.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })

        // 리스트 아이템 클릭 시 해당 글 자세히 보기
        qnaListAdapter.setItemClickListener(object : QnaListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                var qnaId = qnaList[position].qnaId
                /*val bundle = Bundle()
                bundle.putInt("qnaId", qnaId)

                val postFragment = QnaPostFragment()
                postFragment.arguments = bundle
                (activity as NaviActivity).replace(postFragment)*/

                val intent = Intent(context, QnaPostActivity::class.java)
                intent.putExtra("qnaId", qnaId)
                startActivity(intent)
            }
        })

        return binding.root
    }

    // 질문 보기
    private fun showQuestion() {
        binding.questionView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.questionView.adapter = qnaListAdapter

        retrofit.getAllQna().enqueue(object: Callback<List<Qna>> {
            override fun onResponse(call: Call<List<Qna>>, response: Response<List<Qna>>) {
                if (response.isSuccessful) {
                    qnaList.clear()
                    qnaList.addAll(response.body()!!)
                    listItems.clear()
                    for (document in response.body()!!) {
                        val item = document.question
                        if (item != "nan") {
                            listItems.add(item)
                        }
                    }
                    binding.resultCount.text = listItems.size.toString()
                    qnaListAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Qna>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }
}