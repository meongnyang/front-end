package com.example.meongnyang.qna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.meongnyang.R
import com.example.meongnyang.databinding.QnaActivityPostBinding

class QnaPostActivity : AppCompatActivity() {
    private lateinit var binding: QnaActivityPostBinding
    private lateinit var viewModel: QnaPostViewModel
    private lateinit var viewModelFactory: QnaViewModelFactory

    var qnaId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.qna_activity_post)

        val intent = intent
        qnaId = intent.getIntExtra("qnaId", 0)

        viewModelFactory = QnaViewModelFactory(qnaId)
        viewModel = ViewModelProvider(this,
            viewModelFactory).get(QnaPostViewModel::class.java)

        binding.apply {
            qna = viewModel
            lifecycleOwner = this@QnaPostActivity
        }
    }
}