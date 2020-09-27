package com.dev.moskal.postbrowser.app.postlist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dev.moskal.postbrowser.domain.usecase.FetchData
import com.dev.moskal.postbrowser.domain.usecase.GetPosts
import kotlinx.coroutines.launch

class PostListViewModel @ViewModelInject constructor(
    getPosts: GetPosts,
    private val fetchData: FetchData,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val postList = getPosts.execute().asLiveData()

    init {
        viewModelScope.launch {
            fetchData.execute()
        }
    }
}