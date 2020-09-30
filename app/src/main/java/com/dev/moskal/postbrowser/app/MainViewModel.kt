package com.dev.moskal.postbrowser.app

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.moskal.postbrowser.domain.usecase.InitialFetch
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    initialFetch: InitialFetch
) : ViewModel() {

    private val _selectedPost = MutableLiveData<Int>()
    val selectedPost: LiveData<Int>
        get() = _selectedPost

    fun selectPost(postId: Int) {
        _selectedPost.value = postId
    }

    init {
        viewModelScope.launch {
            initialFetch.execute()
        }
    }
}