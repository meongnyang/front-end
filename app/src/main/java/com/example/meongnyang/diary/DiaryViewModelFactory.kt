package com.example.meongnyang.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meongnyang.model.PostDiary

class DiaryViewModelFactory(private val recordId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            return DiaryViewModel(recordId = recordId) as T
        }
        throw IllegalAccessException("Un")
    }
}