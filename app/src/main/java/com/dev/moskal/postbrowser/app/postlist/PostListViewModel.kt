package com.dev.moskal.postbrowser.app.postlist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.moskal.postbrowser.domain.repository.PostRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class PostListViewModel @ViewModelInject constructor(
    private val repo: PostRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        viewModelScope.launch {
            repo.getPosts().collect {
                Timber.i("$it")
            }
        }
    }
}