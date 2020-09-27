package com.dev.moskal.postbrowser.app.postlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.moskal.postbrowser.databinding.PostItemBinding

object PostListViewTypes {
    internal const val TYPE_POST = 0
}

internal class PostViewHolder(
    parent: ViewGroup,
    private val binding: PostItemBinding = PostItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PostListItem.PostItem) {
        binding.item = item
    }
}