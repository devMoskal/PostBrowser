package com.dev.moskal.postbrowser.domain.model

data class Album(
    val id: Int,
    val title: String,
    val photos: List<Photo>
)