package com.dev.moskal.postbrowser.app.postdetail

data class DetailsViewState(
    val items: List<DetailsListItem> = emptyList(),
    val isError: Boolean = false,
    val isPostSelected: Boolean = true,
    val isPostLoading: Boolean = false
) {


    companion object {
        //        fun withAlbumLoading(postTitle: String, body: String) = DetailsViewState(postTitle, body)
        val LOADING = DetailsViewState(isPostLoading = true)
        val ERROR = DetailsViewState(isError = true)
        val NO_POST_SELECTED = DetailsViewState(isPostSelected = false)
    }
}

