package com.mustakimarianto.devpeek.core.di

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.mustakimarianto.devpeek.BuildConfig
import com.mustakimarianto.devpeek.core.data.local.AppDatabase
import com.mustakimarianto.devpeek.core.data.local.PreferencesManager
import com.mustakimarianto.devpeek.core.data.remote.GitHubHeadersInterceptor
import com.mustakimarianto.devpeek.core.data.remote.GithubApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val timeoutInSeconds = 3L
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient().newBuilder().addInterceptor(logging)
            .connectTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutInSeconds, TimeUnit.SECONDS).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().addInterceptor(ChuckerInterceptor(context))
            .addInterceptor(GitHubHeadersInterceptor(BuildConfig.GITHUB_TOKEN)).build()

        return Retrofit.Builder().baseUrl("https://api.github.com/").client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
    }

    @Provides
    @Singleton
    fun provideGithubApi(retrofit: Retrofit): GithubApi {
        return retrofit.create(GithubApi::class.java)
    }
}