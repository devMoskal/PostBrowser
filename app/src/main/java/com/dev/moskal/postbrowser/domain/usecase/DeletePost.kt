package com.dev.moskal.postbrowser.domain.usecase

import com.dev.moskal.postbrowser.domain.Repository
import javax.inject.Inject

class DeletePost @Inject constructor(private val repository: Repository) {
    suspend fun execute(id: Int) = repository.deletePost(id)
}