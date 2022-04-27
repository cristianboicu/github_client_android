package com.cristianboicu.githubclient.data.repository

import androidx.lifecycle.LiveData
import com.cristianboicu.githubclient.data.local.ILocalDataSource
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.data.remote.IRemoteDataSource
import com.cristianboicu.githubclient.utils.Result
import com.cristianboicu.githubclient.utils.Status
import com.cristianboicu.githubclient.utils.wrapEspressoIdlingResource

class DefaultRepository(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource,
) : IDefaultRepository {

    override suspend fun deleteAll() {
        wrapEspressoIdlingResource {
            localDataSource.deleteRepositories()
            localDataSource.deleteUser()
        }
    }

    override suspend fun getUser(): Result<User> {
        wrapEspressoIdlingResource {
            return localDataSource.getUser()
        }
    }

    override suspend fun fetchUser(username: String) =
        wrapEspressoIdlingResource { remoteDataSource.getUser(username) }

    override suspend fun refreshUser(username: String): Status {
        wrapEspressoIdlingResource {
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
    }

    override suspend fun getRepositoryById(id: Long): Result<DbGhRepository> {
        wrapEspressoIdlingResource {
            return localDataSource.getRepositoryById(id)
        }
    }

    override fun observeRepositories(): LiveData<Result<List<DbGhRepository>>> {
        wrapEspressoIdlingResource {
            return localDataSource.observeRepositories()
        }
    }

    override suspend fun refreshRepositories(username: String) {
        wrapEspressoIdlingResource {
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
}