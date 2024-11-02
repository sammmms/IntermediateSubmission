package com.example.intermediatesubmission.data.model

import com.example.intermediatesubmission.data.response.ListStoryItem
import com.example.intermediatesubmission.data.response.Story

open class State(
    var isLoading: Boolean = false,
    var hasError: Boolean = false,
)

class DetailStoryState(
    var data: Story? = null,
    isLoading: Boolean = false,
    hasError: Boolean = false
) :
    State(isLoading, hasError) {
}

class ListStoryState(
    var data: List<ListStoryItem>? = null,
    isLoading: Boolean = false,
    hasError: Boolean = false
) :
    State(isLoading, hasError) {
}
