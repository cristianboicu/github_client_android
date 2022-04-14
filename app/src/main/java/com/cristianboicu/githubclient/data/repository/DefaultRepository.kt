package com.cristianboicu.githubclient.data.repository

import com.cristianboicu.githubclient.data.local.LocalDataSource
import com.cristianboicu.githubclient.data.remote.RemoteDataSource

class DefaultRepository(
    val localDataSource: LocalDataSource,
    val remoteDataSource: RemoteDataSource,
) : IDefaultRepository {
}