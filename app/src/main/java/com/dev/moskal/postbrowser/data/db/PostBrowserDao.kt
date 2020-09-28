package com.dev.moskal.postbrowser.data.db

import androidx.room.*
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_USERS
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

    @Transaction
    @Query("SELECT * FROM $TABLE_USERS")
    fun getPostsInfo(): Flow<List<DbPostAndUser>>
}