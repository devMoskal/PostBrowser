package com.dev.moskal.postbrowser.data.db

import androidx.room.*
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_ALBUMS
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_POSTS
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_USERS
import kotlinx.coroutines.flow.Flow


@Dao
interface PostBrowserDao {

    @Transaction
    suspend fun batchUpdate(
        posts: List<DbPost>, users: List<DbUser>, albums: List<DbAlbum>, photos: List<DbPhoto>,
    ) {
        insertUsers(users)
        insertPosts(posts)
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

    @Query("SELECT * FROM $TABLE_POSTS WHERE postId = :id")
    fun getPost(id: Int): Flow<DbPost?>

    @Transaction
    @Query("SELECT * FROM $TABLE_POSTS")
    fun getPostWithUser(): Flow<List<DbPostWithUser>>

    @Transaction
    @Query("SELECT * FROM $TABLE_ALBUMS WHERE userId = :userId")
    suspend fun getAlbumsWithPhoto(userId: Int): List<DbAlbumWithPhotos>

    // unused in a project, added just to play with db relationships in tests
    @Query("DELETE FROM $TABLE_USERS WHERE userId = :id")
    suspend fun deleteUser(id: Int)
}