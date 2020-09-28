package com.dev.moskal.postbrowser.data.db

import androidx.room.*
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_POST
import kotlinx.coroutines.flow.Flow


@Dao
interface PostBrowserDao {

    @Transaction
    suspend fun batchUpdate(posts: List<DbPost>, users: List<DbUser>) {
        insertPosts(posts)
        insertUsers(users)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<DbPost>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(posts: List<DbUser>)

    @Query("DELETE FROM $TABLE_POST WHERE postId = :id")
    suspend fun deletePost(id: Int)

    @Transaction
    @Query("SELECT * FROM $TABLE_POST")
    fun getPostsInfo(): Flow<List<DbPostWithUser>>
}