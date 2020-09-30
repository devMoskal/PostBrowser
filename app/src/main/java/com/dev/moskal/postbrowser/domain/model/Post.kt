package com.dev.moskal.postbrowser.domain.model

data class Post(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
)

const val POST_NOT_SELECTED_ID = -1