package com.dev.moskal.postbrowser.domain.usecase

import com.dev.moskal.postbrowser.domain.Repository
import javax.inject.Inject

class GetUserAlbums @Inject constructor(val repository: Repository) {

    suspend fun execute(userId: Int) = repository.getUserAlbums(userId)
}