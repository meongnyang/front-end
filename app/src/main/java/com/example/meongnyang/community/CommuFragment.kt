package com.example.meongnyang.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meongnyang.R
import com.example.meongnyang.model.Place
import com.example.meongnyang.model.Post

class CommuFragment : Fragment() {
    private lateinit var postList: ArrayList<Post>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.commu_fragment_main, container, false)
    }
}