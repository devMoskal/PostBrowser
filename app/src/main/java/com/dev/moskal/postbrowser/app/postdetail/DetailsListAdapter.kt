package com.dev.moskal.postbrowser.app.postdetail

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dev.moskal.postbrowser.app.postdetail.AlbumListViewTypes.TYPE_ALBUM
import com.dev.moskal.postbrowser.app.postdetail.AlbumListViewTypes.TYPE_DETAILS
import com.dev.moskal.postbrowser.app.postdetail.AlbumListViewTypes.TYPE_LOADING
import com.dev.moskal.postbrowser.app.postdetail.AlbumListViewTypes.TYPE_PHOTO
import javax.inject.Inject

/**
 * Adapter of [RecyclerView] which is responsible for showing [DetailsListItem]
 *
 * items are provided via DataBinding,
 * using custom binding adapter implemented in companion object
 */
class AlbumListAdapter @Inject constructor() :
    ListAdapter<DetailsListItem, RecyclerView.ViewHolder>(AlbumListDiffUtil()) {
    private lateinit var clickListener: AlbumClickListener

    fun isStickyHeader(itemPosition: Int) =
        itemPosition in 0..itemCount && getItemViewType(itemPosition) == TYPE_ALBUM

    fun onStickyHeaderClick(viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is AlbumViewHolder) {
            viewHolder.onClick()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DETAILS -> DetailsHeaderViewHolder(parent)
            TYPE_ALBUM -> AlbumViewHolder(parent)
            TYPE_PHOTO -> PhotoViewHolder(parent)
            TYPE_LOADING -> LoadingViewHolder(parent)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlbumViewHolder -> holder.bind(
                getItem(position) as DetailsListItem.AlbumItem,
                clickListener
            )
            is PhotoViewHolder -> holder.bind(
                getItem(position) as DetailsListItem.PhotoItem,
            )
            is DetailsHeaderViewHolder -> holder.bind(
                getItem(position) as DetailsListItem.HeaderItem,
            )
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).type

    private fun update(items: List<DetailsListItem>) {
        submitList(items)
    }

    companion object {
        /**
         * DataBinding adapter used to retrieve albums items
         *
         * @param items list of album ready to be presented on screen
         */
        @JvmStatic
        @BindingAdapter("album_list_items")
        fun RecyclerView.bindItems(items: List<DetailsListItem>?) {
            items?.let {
                (adapter as AlbumListAdapter).update(items)
            }
        }

        /**
         * DataBinding adapter for returning click events from adapter
         *
         * @param clickListener hook to notify about clicked item
         */
        @JvmStatic
        @BindingAdapter("album_click_listener")
        fun RecyclerView.bindClickListener(clickListener: AlbumClickListener) {
            (adapter as AlbumListAdapter).clickListener = clickListener
        }
    }
}

class AlbumListDiffUtil : DiffUtil.ItemCallback<DetailsListItem>() {
    override fun areItemsTheSame(oldItem: DetailsListItem, newItem: DetailsListItem) =
        when (oldItem) {
            is DetailsListItem.AlbumItem -> newItem is DetailsListItem.AlbumItem && oldItem.id == newItem.id
            is DetailsListItem.PhotoItem -> newItem is DetailsListItem.PhotoItem && oldItem.url == newItem.url
            DetailsListItem.AlbumLoadingItem -> newItem is DetailsListItem.AlbumLoadingItem
            is DetailsListItem.HeaderItem -> newItem is DetailsListItem.HeaderItem
        }

    override fun areContentsTheSame(
        oldItem: DetailsListItem,
        newItem: DetailsListItem
    ) = oldItem == newItem
}
