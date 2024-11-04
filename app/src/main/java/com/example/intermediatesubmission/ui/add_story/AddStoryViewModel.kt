package com.example.intermediatesubmission.ui.add_story

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.intermediatesubmission.data.model.State

class AddStoryViewModel : ViewModel() {
    private val _state = MutableLiveData<State>()
    val state get() = _state

    fun isLoading() {
        _state.postValue(State(isLoading = true))
    }

    fun initialState() {
        _state.postValue(State())
    }
}