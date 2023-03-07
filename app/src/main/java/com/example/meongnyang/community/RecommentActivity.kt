package com.example.meongnyang.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.meongnyang.R
import com.example.meongnyang.databinding.CommuActivityRecommentBinding

class RecommentActivity : AppCompatActivity() {
    private lateinit var binding: CommuActivityRecommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CommuActivityRecommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        var postId = intent.getIntExtra("postId", 0)

        sendId(postId)

        binding.button.setOnClickListener {
            var comment = binding.contentsEdit.text.toString()
            binding.contentsEdit.text = null
            Log.d("content", comment)
            sendReContent(postId, comment)
        }
    }
    private fun sendId(postId: Int) {
        val bundle = Bundle()
        bundle.putInt("postId", postId)
        val postFragment = PostFragment()
        postFragment.arguments = bundle

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.commentFrameLayout, postFragment).commit()
    }

    private fun sendReContent(postId: Int, contents: String) {
        val bundle = Bundle()
        bundle.putInt("postId", postId)
        bundle.putString("reContents", contents)
        val postFragment = PostFragment()
        postFragment.arguments = bundle

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.commentFrameLayout, postFragment).commit()
    }
}