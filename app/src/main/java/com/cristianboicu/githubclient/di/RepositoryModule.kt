package com.cristianboicu.githubclient.di

import com.cristianboicu.githubclient.data.local.ILocalDataSource
import com.cristianboicu.githubclient.data.local.LocalDataSource
import com.cristianboicu.githubclient.data.remote.IRemoteDataSource
import com.cristianboicu.githubclient.data.remote.RemoteDataSource
import com.cristianboicu.githubclient.data.repository.DefaultRepository
import com.cristianboicu.githubclient.data.repository.IDefaultRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

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
}