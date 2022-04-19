package com.cristianboicu.githubclient.di

import android.content.Context
import androidx.room.Room
import com.cristianboicu.githubclient.data.local.GhDao
import com.cristianboicu.githubclient.data.local.GhDatabase
import com.cristianboicu.githubclient.data.local.LocalDataSource
import com.cristianboicu.githubclient.data.remote.ApiService
import com.cristianboicu.githubclient.data.remote.RemoteDataSource
import com.cristianboicu.githubclient.data.repository.FakeAndroidDefaultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("inMemoryDao")
    fun provideDao(db: GhDatabase) = db.getGhDao()

    @Provides
    @Named("inMemoryDb")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context,
            GhDatabase::class.java
        ).allowMainThreadQueries().build()

    @Provides
    @Named("fakeLocalDataSource")
    fun provideLocalDataSource(dao: GhDao): LocalDataSource =
        LocalDataSource(dao)

    @Provides
    @Named("fakeRemoteDataSource")
    fun provideRemoteDataSource(
        apiService: ApiService,
    ): RemoteDataSource = RemoteDataSource(apiService)

    @Singleton
    @Provides
    @Named("fakeDefaultRepository")
    fun provideDefaultRepository(
    ): FakeAndroidDefaultRepository = FakeAndroidDefaultRepository(mutableListOf(), mutableListOf(), null)
}