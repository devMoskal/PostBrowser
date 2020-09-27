package com.dev.moskal.postbrowser.data.db

import androidx.room.*
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_POST
import kotlinx.coroutines.flow.Flow

@Dao
interface PostBrowserDao {

    @Transaction
    suspend fun batchUpdate(posts: List<DbPost>) {
        insertPosts(posts)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<DbPost>)

    @Query("SELECT * FROM $TABLE_POST")
    fun getPosts(): Flow<List<DbPost>>
}