package com.dev.moskal.postbrowser

import android.content.Context
import androidx.room.Room
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class FakeDbModule {

    @Singleton
    @Provides
    internal fun provideDb(@ApplicationContext appContext: Context) =
        Room.inMemoryDatabaseBuilder(appContext, PostBrowserDatabase::class.java).build()

    @Provides
    internal fun provideDao(db: PostBrowserDatabase) = db.postBrowserDao()
}

