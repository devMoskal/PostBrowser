package com.dev.moskal.postbrowser.app

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.moskal.postbrowser.domain.usecase.FetchData
import kotlinx.coroutines.launch

class MainActivityViewModel @ViewModelInject constructor(
    fetchData: FetchData
) : ViewModel() {

    init {
        viewModelScope.launch {
            fetchData.execute()
        }
    }
}