package com.dev.moskal.postbrowser.data.di


import com.dev.moskal.postbrowser.data.db.DbPostWithUser
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.data.db.mapPostAndUserDbEntityToDomainModel
import com.dev.moskal.postbrowser.data.network.api.AlbumApi
import com.dev.moskal.postbrowser.data.network.api.PhotoApi
import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.dev.moskal.postbrowser.data.network.api.UserApi
import com.dev.moskal.postbrowser.data.network.mapAlbumApiResponseToDbEntity
import com.dev.moskal.postbrowser.data.network.mapPhotoApiResponseToDbEntity
import com.dev.moskal.postbrowser.data.network.mapPostApiResponseToDbEntity
import com.dev.moskal.postbrowser.data.network.mapUserApiResponseToDbEntity
import com.dev.moskal.postbrowser.data.network.response.AlbumApiResponse
import com.dev.moskal.postbrowser.data.network.response.PhotoApiResponse
import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import com.dev.moskal.postbrowser.data.network.response.UserApiResponse
import com.dev.moskal.postbrowser.data.repository.*
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
    internal fun provideMainRepository(
        dao: PostBrowserDao,
        postRepository: PostRepository,
        userRepository: UserRepository,
        photosRepository: PhotosRepository,
        albumRepository: AlbumRepository
    ): Repository = MainRepository(
        dao,
        postRepository,
        userRepository,
        photosRepository,
        albumRepository,
        List<DbPostWithUser>::mapPostAndUserDbEntityToDomainModel
    )

    @Provides
    internal fun providePostRepository(api: PostApi, dao: PostBrowserDao) =
        PostRepository(api, dao, List<PostApiResponse>::mapPostApiResponseToDbEntity)

    @Provides
    internal fun provideUserRepository(api: UserApi) =
        UserRepository(api, List<UserApiResponse>::mapUserApiResponseToDbEntity)

    @Provides
    internal fun provideAlbumRepository(api: AlbumApi) =
        AlbumRepository(api, List<AlbumApiResponse>::mapAlbumApiResponseToDbEntity)

    @Provides
    internal fun providePhotoRepository(api: PhotoApi) =
        PhotosRepository(api, List<PhotoApiResponse>::mapPhotoApiResponseToDbEntity)
}