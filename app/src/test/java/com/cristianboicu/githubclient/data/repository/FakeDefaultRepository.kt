package com.cristianboicu.githubclient.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.utils.Result
import com.cristianboicu.githubclient.utils.Status

class FakeDefaultRepository(
    private var localData: MutableList<DbGhRepository>,
    private var remoteData: List<DbGhRepository>,
    private var user: User?,
) : IDefaultRepository {

    private val observableRepositories = MutableLiveData<List<DbGhRepository>>()

    private var _shouldReturnError = false

    fun shouldReturnError(value: Boolean) {
        _shouldReturnError = value
    }

    override suspend fun getUser(): Result<User> {
        if (_shouldReturnError) {
            return Result.Error(Exception("Unknown error"))
        }
        user?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("No user found"))
    }

    override suspend fun fetchUser(username: String): Result<User> {
        if (_shouldReturnError) {
            return Result.Error(Exception("Check internet connection"))
        }
        return Result.Success(User(0, username, "avatar_url",
            "html_url", "name", 0))
    }

    override suspend fun refreshUser(username: String): Status {
        val result = fetchUser(username)
        if (result is Result.Success) {
            user = result.data
            return Status.SUCCESS
        }
        return Status.ERROR
    }

    override suspend fun getRepositoryById(id: Long): Result<DbGhRepository> {
        val result = localData.find { it.id == id }
        result?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("No such repository"))
    }

    override fun observeRepositories(): LiveData<Result<List<DbGhRepository>>> {
        return observableRepositories.map {
            Result.Success(it)
        }
    }

    override suspend fun refreshRepositories(username: String) {
        if (_shouldReturnError) {
            return
        }
        localData.clear()
        for (item in remoteData) {
            localData.add(item)
        }
        observableRepositories.postValue(localData)
    }
}