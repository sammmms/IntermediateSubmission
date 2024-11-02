package com.example.intermediatesubmission.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.intermediatesubmission.ui.detail_story.DetailStoryViewModel
import com.example.intermediatesubmission.ui.home.HomeViewModel

class ViewModelFactory private constructor(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(context)
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        with(modelClass) {
            return when {
                isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(context) as T
                isAssignableFrom(DetailStoryViewModel::class.java) -> DetailStoryViewModel(context) as T
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}