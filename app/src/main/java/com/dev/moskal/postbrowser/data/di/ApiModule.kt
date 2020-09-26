package com.dev.moskal.postbrowser.data.di


import com.dev.moskal.postbrowser.BuildConfig
import com.dev.moskal.postbrowser.data.network.api.API_ENDPOINT
import com.dev.moskal.postbrowser.data.network.api.PostApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class ApiModule {

    @Provides
    @Singleton
    internal fun provideWorkApi(retrofit: Retrofit) = retrofit.create(PostApi::class.java)

    @Provides
    @Singleton
    internal fun provideGson() = GsonBuilder().create()

    @Provides
    @Singleton
    internal fun provideOkhttp(logInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) addInterceptor(logInterceptor)
        }
        .build()

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(API_ENDPOINT)
            .client(okHttpClient)
            .build()
    }
}