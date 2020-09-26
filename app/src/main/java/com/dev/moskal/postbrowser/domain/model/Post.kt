package com.dev.moskal.postbrowser.domain.model

data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
)

data class User(
    val id: Int,
    val email: String,
)