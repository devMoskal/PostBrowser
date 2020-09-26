package com.dev.moskal.postbrowser.data.di


import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.dev.moskal.postbrowser.data.network.mapper.mapPosts
import com.dev.moskal.postbrowser.data.repository.PostRepositoryImpl
import com.dev.moskal.postbrowser.domain.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {

    @Provides
    internal fun providePostRepository(api: PostApi): PostRepository =
        PostRepositoryImpl(api, ::mapPosts)


}