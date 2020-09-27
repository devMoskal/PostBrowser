package com.dev.moskal.postbrowser.data.di


import com.dev.moskal.postbrowser.data.db.DbPost
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.data.db.mapPostDbEntityToDomainModel
import com.dev.moskal.postbrowser.data.db.mapPostDomainModelToDbEntity
import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.dev.moskal.postbrowser.data.network.mapper.mapPostApiResponseToDomainModel
import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import com.dev.moskal.postbrowser.data.repository.PostRepository
import com.dev.moskal.postbrowser.data.repository.RepositoryImpl
import com.dev.moskal.postbrowser.domain.Repository
import com.dev.moskal.postbrowser.domain.model.Post
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
        postRepository: PostRepository
    ): Repository = RepositoryImpl(dao, postRepository)

    @Provides
    internal fun providePostRepository(api: PostApi, dao: PostBrowserDao) =
        PostRepository(
            api,
            dao,
            List<PostApiResponse>::mapPostApiResponseToDomainModel,
            List<Post>::mapPostDomainModelToDbEntity,
            List<DbPost>::mapPostDbEntityToDomainModel,
        )
}