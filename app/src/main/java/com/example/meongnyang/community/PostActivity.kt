package com.example.meongnyang.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.databinding.CommuActivityPostBinding
import com.example.meongnyang.model.Comment
import com.example.meongnyang.model.Id
import com.example.meongnyang.model.PetModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostActivity : AppCompatActivity() {
    private lateinit var binding: CommuActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CommuActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        var postId = intent.getIntExtra("postId", 0)

        sendId(postId)

        binding.button.setOnClickListener {
            var comment = binding.contentsEdit.text.toString()
            binding.contentsEdit.text = null
            Log.d("content", comment)
            sendContent(postId, comment)
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