package com.dev.moskal.postbrowser.app.postlist

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dev.moskal.postbrowser.app.postlist.PostListViewTypes.TYPE_LOADING
import com.dev.moskal.postbrowser.app.postlist.PostListViewTypes.TYPE_POST
import javax.inject.Inject

class PostListAdapter @Inject constructor() :
    PagingDataAdapter<PostListItem, RecyclerView.ViewHolder>(COMPARATOR) {

    private lateinit var clickListener: PostItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_POST -> PostViewHolder(parent)
            else -> LoadingViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> holder.bind(
                getItem(position) as PostListItem.PostItem,
                clickListener
            )
        }
    }

    override fun getItemViewType(position: Int) = getItem(position)?.type ?: TYPE_LOADING

    companion object {

        /**
         * DataBinding adapter for returning click events from [PostListAdapter]
         *
         * @param clickListener hook to notify about clicked item
         */
        @JvmStatic
        @BindingAdapter("click_listener")
        fun RecyclerView.bindClickListener(clickListener: PostItemClickListener) {
            (adapter as PostListAdapter).clickListener = clickListener
        }

        private val COMPARATOR = object : DiffUtil.ItemCallback<PostListItem>() {
            override fun areItemsTheSame(oldItem: PostListItem, newItem: PostListItem) =
                when (oldItem) {
                    is PostListItem.PostItem -> newItem is PostListItem.PostItem && oldItem.id == newItem.id
                    PostListItem.PostLoadingItem -> newItem is PostListItem.PostLoadingItem
                }

            override fun areContentsTheSame(
                oldItem: PostListItem,
                newItem: PostListItem
            ) = oldItem == newItem
        }
    }
}


