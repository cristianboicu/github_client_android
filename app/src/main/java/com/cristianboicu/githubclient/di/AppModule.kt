package com.cristianboicu.githubclient.di

import android.content.Context
import androidx.room.Room
import com.cristianboicu.githubclient.data.local.GhDao
import com.cristianboicu.githubclient.data.local.GhDatabase
import com.cristianboicu.githubclient.data.local.ILocalDataSource
import com.cristianboicu.githubclient.data.local.LocalDataSource
import com.cristianboicu.githubclient.data.remote.ApiService
import com.cristianboicu.githubclient.data.remote.IRemoteDataSource
import com.cristianboicu.githubclient.data.remote.RemoteDataSource
import com.cristianboicu.githubclient.data.repository.DefaultRepository
import com.cristianboicu.githubclient.data.repository.IDefaultRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindLocalDataSource(
        localDataSource: LocalDataSource,
    ): ILocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(
        remoteDataSource: RemoteDataSource,
    ): IRemoteDataSource

    @Binds
    abstract fun bindDefaultRepository(
        defaultRepository: DefaultRepository,
    ): IDefaultRepository

    companion object {

        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context,
        ) = Room.databaseBuilder(
            context.applicationContext,
            GhDatabase::class.java,
            "user"
        ).build()

        @Singleton
        @Provides
        fun provideDao(db: GhDatabase) = db.getGhDao()

        @Provides
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        fun provideApiClient(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }

        @Singleton
        @Provides
        fun provideLocalDataSource(ghDao: GhDao) = LocalDataSource(ghDao)

        @Singleton
        @Provides
        fun provideRemoteDataSource(apiService: ApiService) = RemoteDataSource(apiService)

        @Singleton
        @Provides
        fun provideDefaultRepository(
            localDataSource: LocalDataSource,
            remoteDataSource: RemoteDataSource,
        ) = DefaultRepository(localDataSource, remoteDataSource)

    }
}