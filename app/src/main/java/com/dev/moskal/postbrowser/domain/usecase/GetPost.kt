package com.dev.moskal.postbrowser.domain.usecase

import com.dev.moskal.postbrowser.domain.Repository
import javax.inject.Inject

class GetPost @Inject constructor(val repository: Repository) {

    suspend fun execute(id: Int) = repository.getPost(id)
}