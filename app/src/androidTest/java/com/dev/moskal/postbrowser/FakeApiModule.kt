package com.dev.moskal.postbrowser

import com.dev.moskal.postbrowser.data.network.api.AlbumApi
import com.dev.moskal.postbrowser.data.network.api.PhotoApi
import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.dev.moskal.postbrowser.data.network.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * Hilt make UI testing soo easy
 */
@Module
@InstallIn(ApplicationComponent::class)
class TestApiModule {
    @Provides
    @Singleton
    internal fun providePostApi() = object : PostApi {
        override suspend fun getPosts() = testApiPosts
    }

    @Provides
    @Singleton
    internal fun provideUserApi() = object : UserApi {
        override suspend fun getUsers() = testApiUsers
    }

    @Provides
    @Singleton
    internal fun providePhotoApi() = object : PhotoApi {
        override suspend fun getPhotos() = testApiPhotos
    }

    @Provides
    @Singleton
    internal fun provideAlbumApi() = object : AlbumApi {
        override suspend fun getAlbums() = testApiAlbums
    }
}

