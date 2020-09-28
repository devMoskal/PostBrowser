package com.dev.moskal.postbrowser.app.postlist

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dev.moskal.postbrowser.app.postlist.PostListViewTypes.TYPE_LOADING
import com.dev.moskal.postbrowser.app.postlist.PostListViewTypes.TYPE_POST
import javax.inject.Inject

/**
 * Adapter of [RecyclerView] which is responsible for showing [PostListItem]
 *
 * Both [items] are provided via DataBinding,
 * using custom adapter implemented in companion object
 */
class PostListAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<PostListItem> = emptyList()
    private lateinit var clickListener: PostItemClickListener

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_POST -> PostViewHolder(parent)
            TYPE_LOADING -> LoadingViewHolder(parent)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> holder.bind(
                items[position] as PostListItem.PostItem,
                clickListener
            )
        }
    }

    override fun getItemViewType(position: Int) = items[position].type

    private fun update(items: List<PostListItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    companion object {

        /**
         * DataBinding adapter used to retrieve posts items
         *
         * @param items list of post ready to be presented on screen
         */
        @JvmStatic
        @BindingAdapter("post_list_items")
        fun RecyclerView.bindItems(items: List<PostListItem>?) {
            items?.let {
                (adapter as PostListAdapter).update(items)
            }
        }

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
    }
}

