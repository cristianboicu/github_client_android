package com.cristianboicu.githubclient.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.utils.Result
import com.cristianboicu.githubclient.utils.Result.Error
import com.cristianboicu.githubclient.utils.Result.Success

class FakeLocalDataSource(
    private var repositories: MutableList<DbGhRepository>,
    private var user: User?,
) : ILocalDataSource {

    private var observableRepositories = MutableLiveData<List<DbGhRepository>>(repositories)

    private var shouldReturnError = false

    fun shouldReturnError(value: Boolean) {
        shouldReturnError = value
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

    override suspend fun saveUser(user: User) {
        this.user = user
    }

    override suspend fun deleteUser() {
        user = null
    }

    override suspend fun deleteRepositories() {
        repositories.clear()
        observableRepositories.postValue(repositories)
    }

    override suspend fun saveRepositories(repositories: List<DbGhRepository>) {
        this.repositories = repositories.toMutableList()
        observableRepositories.postValue(this.repositories)
    }

    override fun observeRepositories(): LiveData<Result<List<DbGhRepository>>> {
        return observableRepositories.map {
            Success(it)
        }
    }

    override suspend fun getRepositoryById(id: Long): Result<DbGhRepository> {
        val res = repositories.find { it.id == id }
        return if (shouldReturnError) {
            Error(Exception("Unknown error"))
        } else {
            res?.let {
                Success(it)
            } ?: Error(Exception("No such repository"))
        }
    }
}