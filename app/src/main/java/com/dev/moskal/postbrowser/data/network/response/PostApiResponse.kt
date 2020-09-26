package com.dev.moskal.postbrowser.data.network.response

import com.google.gson.annotations.SerializedName

data class PostApiResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val userId: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("body") val body: String?
)