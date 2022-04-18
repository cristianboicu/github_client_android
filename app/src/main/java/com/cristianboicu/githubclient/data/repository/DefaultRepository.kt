package com.cristianboicu.githubclient.data.repository

import androidx.lifecycle.LiveData
import com.cristianboicu.githubclient.data.local.LocalDataSource
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.data.remote.RemoteDataSource
import com.cristianboicu.githubclient.utils.Result
import com.cristianboicu.githubclient.utils.Status

class DefaultRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : IDefaultRepository {

    override suspend fun getUser(): Result<User> {
        return localDataSource.getUser()
    }

    override suspend fun fetchUser(username: String) = remoteDataSource.getUser(username)

    override suspend fun refreshUser(username: String): Status {
        val fetchedUser = fetchUser(username)
        return if (fetchedUser is Result.Success) {
            try {
                localDataSource.deleteUser()
                localDataSource.saveUser(fetchedUser.data)
                Status.SUCCESS
            } catch (e: Exception) {
                Status.ERROR
            }
        } else {
            Status.ERROR
        }
    }

    override suspend fun getRepositoryById(id: Long): Result<DbGhRepository> {
        return localDataSource.getRepositoryById(id)
    }

    override fun observeRepositories(): LiveData<Result<List<DbGhRepository>>> {
        return localDataSource.observeRepositories()
    }

    override suspend fun refreshRepositories(username: String) {
        val fetchedRepositories = remoteDataSource.getUserRepositories(username)
        if (fetchedRepositories is Result.Success) {
            try {
                localDataSource.deleteRepositories()
                localDataSource.saveRepositories(fetchedRepositories.data)
            } catch (e: Exception) {
            }
        }
    }
}