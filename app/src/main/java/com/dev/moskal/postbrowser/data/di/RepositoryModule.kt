package com.dev.moskal.postbrowser.data.di


import com.dev.moskal.postbrowser.data.db.DbPostAndUser
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.data.db.mapPostAndUserDbEntityToDomainModel
import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.dev.moskal.postbrowser.data.network.api.UserApi
import com.dev.moskal.postbrowser.data.network.mapPostApiResponseToDbEntity
import com.dev.moskal.postbrowser.data.network.mapUserApiResponseToDbEntity
import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import com.dev.moskal.postbrowser.data.network.response.UserApiResponse
import com.dev.moskal.postbrowser.data.repository.PostRepository
import com.dev.moskal.postbrowser.data.repository.RepositoryImpl
import com.dev.moskal.postbrowser.data.repository.UserRepository
import com.dev.moskal.postbrowser.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    internal fun provideRepository(
        dao: PostBrowserDao,
        postRepository: PostRepository,
        userRepository: UserRepository
    ): Repository = RepositoryImpl(
        dao,
        postRepository,
        userRepository,
        List<DbPostAndUser>::mapPostAndUserDbEntityToDomainModel
    )

    @Provides
    internal fun providePostRepository(api: PostApi, dao: PostBrowserDao) =
        PostRepository(
            api,
            dao,
            List<PostApiResponse>::mapPostApiResponseToDbEntity
        )

    @Provides
    internal fun provideUserRepository(api: UserApi, dao: PostBrowserDao) =
        UserRepository(
            api,
            List<UserApiResponse>::mapUserApiResponseToDbEntity
        )
}