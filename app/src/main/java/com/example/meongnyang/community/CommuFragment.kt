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
import com.example.meongnyang.model.Post

class CommuFragment : Fragment() {
    private lateinit var binding: CommuFragmentMainBinding
    private val model: CommuViewModel by activityViewModels()
    //private lateinit var postList: ArrayList<Post>
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
        // Inflate the layout for this fragment
        return binding.root
    }
}