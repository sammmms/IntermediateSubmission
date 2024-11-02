package com.example.intermediatesubmission.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.intermediatesubmission.data.model.ListStoryState
import com.example.intermediatesubmission.data.response.ListStoryItem
import com.example.intermediatesubmission.data.response.StoryResponse
import com.example.intermediatesubmission.data.retrofit.ApiConfig
import com.example.intermediatesubmission.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(context: Context) : ViewModel() {
    private var apiService: ApiService = ApiConfig.getApiService(context)

    private val _state = MutableLiveData<ListStoryState>()
    val state get() = _state

    private fun setListStory(listStory: List<ListStoryItem>) {
        _state.postValue(
            ListStoryState(
                data = listStory
            )
        )
    }

    init {
        getListStory()
    }

    fun getListStory() {
        val client = apiService.getAllStory()
        _state.postValue(ListStoryState(isLoading = true))
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    setListStory(response.body()?.listStory as List<ListStoryItem>)
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e("HomeViewModel", "onFailure: ${t.message}")
                _state.postValue(ListStoryState(hasError = true))
            }
        })
    }
}