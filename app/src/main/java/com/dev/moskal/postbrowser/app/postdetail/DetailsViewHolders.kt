package com.dev.moskal.postbrowser.app.postdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.dev.moskal.postbrowser.R
import com.dev.moskal.postbrowser.databinding.AlbumItemBinding
import com.dev.moskal.postbrowser.databinding.DetailsHeaderItemBinding
import com.dev.moskal.postbrowser.databinding.PhotoItemBinding

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

internal class LoadingViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(inflateView(parent)) {

    companion object {
        private fun inflateView(parent: ViewGroup) =
            LayoutInflater.from(parent.context).inflate(R.layout.post_loading_item, parent, false)
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
    }

    companion object {
        /**
         * custom binding of image using [Glide]. It's handy solution to load image asynchronously,
         * aware of ImageView lifecycle and with out-of-the-box cache
         */
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            if (url == null) return

            // workaround for header issue while using https://via.placeholder.com
            val glideUrl = GlideUrl(
                url,
                LazyHeaders.Builder()
                    .addHeader(HEADER_KEY_USER_AGENT, WebSettings.getDefaultUserAgent(view.context))
                    .build()
            )
            Glide.with(view.context)
                .load(glideUrl)
                .error(R.drawable.photo_error_placeholder)
                .placeholder(R.drawable.loading_placeholder)
                .into(view)
        }

        private const val HEADER_KEY_USER_AGENT = "User-Agent"
    }
}