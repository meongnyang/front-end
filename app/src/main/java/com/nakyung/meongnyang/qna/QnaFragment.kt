package com.nakyung.meongnyang.qna

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.databinding.QnaFragmentMainBinding
import com.nakyung.meongnyang.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QnaFragment : Fragment() {
    private lateinit var binding: QnaFragmentMainBinding
    //private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

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

        /*var watcher = MyEditWatcher()
        binding.searchEdit.addTextChangedListener(watcher)*/

        qnaList = arrayListOf()

        binding.questionView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.questionView.adapter = qnaListAdapter

        showQuestion()

        binding.searchBtn.setOnClickListener {
            search(binding.searchEdit.text.toString())
        }

        // 키보드 올라 왔을 때 화면 가리는 것 방지하는 코드
        /*keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = { keyboardHeight ->
                binding.QnaSv.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })*/

        // 리스트 아이템 클릭 시 해당 글 자세히 보기
        qnaListAdapter.setItemClickListener(object : QnaListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                var qnaId = qnaList[position].qnaId

                val intent = Intent(context, QnaPostActivity::class.java)
                intent.putExtra("qnaId", qnaId)
                startActivity(intent)
            }
        })

        return binding.root
    }

    // 질문 보기
    private fun showQuestion() {
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
                    binding.resultCount.text = "검색 결과 ${listItems.size}건"
                    qnaListAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Qna>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }
        })
    }

    private fun search(word: String) {
        retrofit.getAllQna().enqueue(object: Callback<List<Qna>> {
            override fun onResponse(call: Call<List<Qna>>, response: Response<List<Qna>>) {
                qnaList.clear()
                listItems.clear()
                for (document in response.body()!!) {
                    val item = document.question
                    if (item.contains(word)) {
                        qnaList.add(document)
                        listItems.add(item)
                    }
                }
                binding.resultCount.text = "검색 결과 ${listItems.size}건"
                qnaListAdapter.notifyDataSetChanged()
                //Toast.makeText(context, "검색 완료!", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<Qna>>, t: Throwable) {
                Toast.makeText(context, "정보 불러오기 실패!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // editText 실시간 입력
    inner class MyEditWatcher : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        // 문자열이 바뀐 후
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            search(p0.toString())
        }

        override fun afterTextChanged(p0: Editable?) {
            search(p0.toString())
        }
    }
}