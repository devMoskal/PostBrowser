package com.dev.moskal.postbrowser.app.postdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.moskal.postbrowser.R
import com.dev.moskal.postbrowser.databinding.AlbumItemBinding
import com.dev.moskal.postbrowser.databinding.DetailsHeaderItemBinding
import com.dev.moskal.postbrowser.databinding.PhotoItemBinding
import timber.log.Timber

internal class DetailsHeaderViewHolder(
    parent: ViewGroup,
    private val binding: DetailsHeaderItemBinding = DetailsHeaderItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DetailsListItem.HeaderItem) {
        binding.item = item
    }
}

internal class AlbumViewHolder(
    parent: ViewGroup,
    private val binding: AlbumItemBinding = AlbumItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DetailsListItem.AlbumItem) {
        binding.item = item
    }
}

internal class PhotoViewHolder(
    parent: ViewGroup,
    private val binding: PhotoItemBinding = PhotoItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DetailsListItem.PhotoItem) {
        binding.item = item
        Timber.i("### haha")
    }
}


internal class LoadingViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(inflateView(parent)) {

    companion object {
        private fun inflateView(parent: ViewGroup) =
            LayoutInflater.from(parent.context).inflate(R.layout.post_loading_item, parent, false)
    }
}