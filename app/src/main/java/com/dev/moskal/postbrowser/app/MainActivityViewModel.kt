package com.dev.moskal.postbrowser.app

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.moskal.postbrowser.domain.usecase.InitialFetch
import kotlinx.coroutines.launch

class MainActivityViewModel @ViewModelInject constructor(
    initialFetch: InitialFetch
) : ViewModel() {

    init {
        viewModelScope.launch {
            initialFetch.execute()
        }
    }
}