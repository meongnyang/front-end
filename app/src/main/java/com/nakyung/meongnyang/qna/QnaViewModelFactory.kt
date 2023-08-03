package com.nakyung.meongnyang.qna

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class QnaViewModelFactory(private val qnaId: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QnaPostViewModel::class.java)) {
            return QnaPostViewModel(qnaId = qnaId) as T
        }
        throw IllegalAccessException("Un")
    }
}