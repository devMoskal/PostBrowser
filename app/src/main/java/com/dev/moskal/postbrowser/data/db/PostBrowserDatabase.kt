package com.dev.moskal.postbrowser.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DB_NAME = "post_browser_database"

@Database(entities = [(DbPost::class), (DbUser::class)], version = 1)
abstract class PostBrowserDatabase : RoomDatabase() {

    abstract fun postBrowserDao(): PostBrowserDao

    companion object {
        fun create(context: Context): PostBrowserDatabase {

            return Room.databaseBuilder(
                context,
                PostBrowserDatabase::class.java,
                DB_NAME
            ).build()
        }

        internal const val TABLE_POST = "post_table"
        internal const val TABLE_USERS = "user_table"
    }
}