package com.dev.moskal.postbrowser.app.postdetail

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

/**
 * Encapsulate possible states of the view
 */
sealed class DetailsViewState(val items: List<DetailsListItem> = emptyList()) {
    class DataLoaded(items: List<DetailsListItem>) : DetailsViewState(items)
    object LoadingState : DetailsViewState()
    object ErrorState : DetailsViewState()
    object PostNotSelectedState : DetailsViewState()

    companion object {
        // set of helper binding adapters

        @JvmStatic
        @BindingAdapter("errorVisibility")
        fun View.errorVisibility(viewState: DetailsViewState) {
            isVisible = viewState is ErrorState
        }
    }
}

