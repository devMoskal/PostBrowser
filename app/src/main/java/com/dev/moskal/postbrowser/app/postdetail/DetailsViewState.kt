package com.dev.moskal.postbrowser.app.postdetail

data class DetailsViewState(
    val items: List<DetailsListItem> = emptyList(),
    val isError: Boolean = false,
    val isPostNotSelected: Boolean = false,
    val isLoading: Boolean = false
) {
    companion object {
        val LOADING = DetailsViewState(isLoading = true)
        val ERROR = DetailsViewState(isError = true)
        val NO_POST_SELECTED = DetailsViewState(isPostNotSelected = true)
    }
}

