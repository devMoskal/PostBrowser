package com.dev.moskal.postbrowser.app.postdetail

import com.dev.moskal.postbrowser.app.postdetail.DetailsListItem.AlbumItem
import com.dev.moskal.postbrowser.app.postdetail.DetailsListItem.PhotoItem
import com.dev.moskal.postbrowser.app.postdetail.DetailsViewState.DataLoaded
import com.dev.moskal.postbrowser.domain.model.Album
import com.dev.moskal.postbrowser.domain.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *
 * Reducer converting data to view state. It implement mechanism to support
 * expandable/foldable header.
 *
 * Typically expandable/foldable headers are implement in recycler view and adapter.
 * However moving solution from view layer to presentation has some benefits:
 * - speed of implementation
 * - one source of truth for items
 * - reactive approach
 * - out-of-the-box support for configuration change.
 *
 * Solution was implemented just for sake of coding challenge
 * so for sure it can be further improved and need to be harden and unit-tested(!)
 *
 * TODO: further refactor solution to be more generic, add tests
 */
class AlbumListReducer @Inject constructor() {

    private val cachedFoldableItems = HashMap<AlbumItemId, List<PhotoItem>>()

    fun reducePost(post: Post?) = post?.let {
        DataLoaded(listOf(DetailsListItem.HeaderItem(it)))
    } ?: DetailsViewState.PostNotSelectedState

    suspend fun reduceAlbums(header: DetailsListItem?, albums: List<Album>) =
        withContext(Dispatchers.Default) {
            DataLoaded(convertAlbumsToItems(header, albums))
        }

    suspend fun reduceAfterAlbumClick(list: List<DetailsListItem>, album: AlbumItem) =
        withContext(Dispatchers.Default) {
            val albumIndex = list.indexOf(album)
            val itemList = list.toMutableList()

            if (albumIndex != -1) {
                itemList[albumIndex] = album.copy(isExpanded = !album.isExpanded)
                if (album.isExpanded) {
                    val iterator = itemList.listIterator(albumIndex + 1)
                    while (iterator.hasNext()) {
                        val item = iterator.next()
                        if (item is AlbumItem) break
                        if (item is PhotoItem) {
                            iterator.remove()
                        }
                    }
                } else {
                    cachedFoldableItems[album.id]?.let { itemList.addAll(albumIndex + 1, it) }
                }
            }

            DataLoaded(itemList)
        }


    private fun convertAlbumsToItems(
        header: DetailsListItem?,
        albums: List<Album>
    ): List<DetailsListItem> {
        val result = mutableListOf<DetailsListItem>()
        header?.let(result::add)
        if (albums.isNotEmpty()) {
            result.add(DetailsListItem.AlbumsLabelItem)
        }
        albums.forEach { album ->
            val albumItem = AlbumItem(album)
            result.add(albumItem)
            val elements = album.photos.map { PhotoItem(it, isVisible = albumItem.isExpanded) }
            cachedFoldableItems[albumItem.id] = elements
        }
        return result
    }


}
