package com.dev.moskal.postbrowser.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase.Companion.TABLE_POST

@Entity(tableName = TABLE_POST)
data class DbPost(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)