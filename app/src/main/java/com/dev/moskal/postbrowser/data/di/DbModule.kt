package com.dev.moskal.postbrowser.data.di


import android.content.Context
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class DbModule {

    @Singleton
    @Provides
    internal fun provideDb(@ApplicationContext appContext: Context) =
        PostBrowserDatabase.create(appContext)

    @Provides
    internal fun provideDao(db: PostBrowserDatabase) = db.postBrowserDao()
}