package com.example.meongnyang.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.meongnyang.R
import com.example.meongnyang.databinding.CommuActivityCommentBinding

class CommentActivity : AppCompatActivity() {
    private lateinit var binding: CommuActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CommuActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        var postId = intent.getIntExtra("postId", 0)

        sendId(postId)

        binding.button.setOnClickListener {
            var comment = binding.contentsEdit.text.toString()
            if (comment == "") {
                Toast.makeText(this, "내용을 입력해 주세요!", Toast.LENGTH_SHORT).show()
            } else {
                binding.contentsEdit.text = null
                Log.d("comment", comment)
                sendContent(postId, comment)
            }
        }
    }
    fun sendId(postId: Int) {
        val bundle = Bundle()
        bundle.putInt("postId", postId)
        val postFragment = PostFragment()
        postFragment.arguments = bundle

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.commentFrameLayout, postFragment).commit()
    }

    private fun sendContent(postId: Int, contents: String) {
        val bundle = Bundle()
        bundle.putInt("postId", postId)
        bundle.putString("contents", contents)
        val postFragment = PostFragment()
        postFragment.arguments = bundle

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.commentFrameLayout, postFragment).commit()
    }
}