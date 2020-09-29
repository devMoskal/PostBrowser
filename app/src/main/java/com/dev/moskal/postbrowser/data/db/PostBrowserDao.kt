package com.dev.moskal.postbrowser.data.db

import androidx.room.*
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_POSTS
import kotlinx.coroutines.flow.Flow


@Dao
interface PostBrowserDao {

    @Transaction
    suspend fun batchUpdate(
        posts: List<DbPost>, users: List<DbUser>, albums: List<DbAlbum>, photos: List<DbPhoto>,
    ) {
        insertPosts(posts)
        insertUsers(users)
        insertAlbums(albums)
        insertPhotos(photos)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<DbPost>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(posts: List<DbUser>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(posts: List<DbPhoto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(posts: List<DbAlbum>)

    @Query("DELETE FROM $TABLE_POSTS WHERE postId = :id")
    suspend fun deletePost(id: Int)

    @Transaction
    @Query("SELECT * FROM $TABLE_POSTS")
    fun getPostsInfo(): Flow<List<DbPostWithUser>>
}