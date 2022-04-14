package com.cristianboicu.githubclient.data.remote

import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.utils.Result


interface IRemoteDataSource {
    suspend fun getUser(username: String): Result<User>
    suspend fun getUserRepositories(username: String): Result<List<DbGhRepository>>
}