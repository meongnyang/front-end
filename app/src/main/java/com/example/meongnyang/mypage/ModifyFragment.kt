package com.example.meongnyang.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.meongnyang.NaviActivity
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.databinding.CommuFragmentPostBinding
import com.example.meongnyang.model.Update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ModifyFragment : Fragment() {
    private lateinit var editName: EditText
    private lateinit var saveBtn: Button
    private lateinit var profile: de.hdodenhof.circleimageview.CircleImageView
    private lateinit var editText: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mypage_fragment_modify, container, false)
        val retrofit = RetrofitApi.create()

        editName = view.findViewById(R.id.editName)
        saveBtn = view.findViewById(R.id.saveBtn)
        profile = view.findViewById(R.id.profile)
        editText = view.findViewById(R.id.editText)

        saveBtn.setOnClickListener {
            var nickname = editName.text.toString()
            retrofit.memberUpdate(nickname).enqueue(object : Callback<Update> {
                override fun onFailure(call: Call<Update>, t: Throwable) {
                    Toast.makeText(context, "변경 실패, 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Update>, response: Response<Update>) {
                    (activity as NaviActivity).replace(MyFragment())
                }
            })

        }

        return view
    }
}