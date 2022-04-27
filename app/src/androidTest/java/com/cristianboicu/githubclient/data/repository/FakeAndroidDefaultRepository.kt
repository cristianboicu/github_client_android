package com.cristianboicu.githubclient.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.utils.Result
import com.cristianboicu.githubclient.utils.Result.Error
import com.cristianboicu.githubclient.utils.Result.Success
import com.cristianboicu.githubclient.utils.Status

class FakeAndroidDefaultRepository constructor(
    private val localRepositories: MutableList<DbGhRepository>,
    private var remoteRepositories: MutableList<DbGhRepository>,
    private var user: User?,
) : IDefaultRepository {

    init {
        Log.d("TEST", "Fake repos")
    }

    private var shouldReturnError = false
    private var observableRepositories = MutableLiveData<List<DbGhRepository>>(localRepositories)

    fun shouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun setUpRepositories(repositories: MutableList<DbGhRepository>) {
        remoteRepositories = repositories
    }

    override suspend fun deleteAll() {
        localRepositories.clear()
        user = null
    }

    override suspend fun getUser(): Result<User> {
        return if (shouldReturnError) {
            Error(Exception("Unknown error"))
        } else {
            user?.let {
                Success(it)
            } ?: Error(Exception("No such user"))
        }
    }

    override suspend fun fetchUser(username: String): Result<User> {
        return if (shouldReturnError) {
            Error(Exception("Error"))
        } else {
            Success(User(22, username, "", "", username, 3))
        }
    }

    override suspend fun refreshUser(username: String): Status {
        val fetchedUser = fetchUser(username)
        return if (fetchedUser is Success) {
            user = null
            user = fetchedUser.data
            Status.SUCCESS
        } else {
            Status.ERROR
        }
    }

    override suspend fun getRepositoryById(id: Long): Result<DbGhRepository> {
        val repository: DbGhRepository? = localRepositories.find { it.id == id }
        return repository?.let {
            Success(repository)
        } ?: Error(Exception("Inexplicable error :("))
    }

    override fun observeRepositories(): LiveData<Result<List<DbGhRepository>>> {
        return observableRepositories.map {
            Success(it)
        }
    }

    override suspend fun refreshRepositories(username: String) {
        if (!shouldReturnError) {
            localRepositories.clear()
            for (item in remoteRepositories) {
                localRepositories.add(item)
            }
            observableRepositories.postValue(localRepositories)
        }
    }
}