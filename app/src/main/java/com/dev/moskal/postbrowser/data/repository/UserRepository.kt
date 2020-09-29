package com.dev.moskal.postbrowser.data.repository

import com.dev.moskal.postbrowser.data.db.DbUser
import com.dev.moskal.postbrowser.data.network.api.UserApi
import com.dev.moskal.postbrowser.data.network.response.UserApiResponse

internal class UserRepository constructor(
    private val userApi: UserApi,
    private val mapApiResponseToDbEntity: List<UserApiResponse>.() -> List<DbUser>,
) {
    suspend fun fetchData(): List<DbUser> = userApi.getUsers().mapApiResponseToDbEntity()
}