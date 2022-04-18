package com.cristianboicu.githubclient.data.remote

import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.utils.Result
import com.cristianboicu.githubclient.utils.Result.Error
import com.cristianboicu.githubclient.utils.Result.Success

class FakeRemoteDataSource(
    private val repositories: List<DbGhRepository>,
) : IRemoteDataSource {

    private var shouldReturnError = false

    fun shouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getUser(username: String): Result<User> {
        return if (shouldReturnError) {
            Error(Exception("No such user"))
        } else {
            Success(User(10, username, "", "", "", 0))
        }
    }

    override suspend fun getUserRepositories(username: String): Result<List<DbGhRepository>> {
        return if (shouldReturnError) {
            Error(Exception("No such user"))
        } else {
            Success(repositories)
        }
    }
}