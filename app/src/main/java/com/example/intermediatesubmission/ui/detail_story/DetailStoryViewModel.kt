package com.example.intermediatesubmission.ui.detail_story

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.intermediatesubmission.data.model.DetailStoryState
import com.example.intermediatesubmission.data.response.DetailStoryResponse
import com.example.intermediatesubmission.data.response.Story
import com.example.intermediatesubmission.data.retrofit.ApiConfig

class DetailStoryViewModel(context: Context) : ViewModel() {
    private val apiService = ApiConfig.getApiService(context)

    private val _detailStoryState = MutableLiveData<DetailStoryState>()
    val state get() = _detailStoryState


    private fun setStoryDetail(story: Story) {
        _detailStoryState.postValue(
            DetailStoryState(
                data = story
            )
        )
    }

    fun getDetailStory(id: String) {
        val client = apiService.getStory(id)
        _detailStoryState.postValue(DetailStoryState(isLoading = true))
        client.enqueue(object : retrofit2.Callback<DetailStoryResponse> {
            override fun onResponse(
                call: retrofit2.Call<DetailStoryResponse>,
                response: retrofit2.Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    setStoryDetail(response.body()?.story as Story)
                }
                
            }

            override fun onFailure(call: retrofit2.Call<DetailStoryResponse>, t: Throwable) {
                _detailStoryState.postValue(DetailStoryState(hasError = true))
            }
        })

    }
}