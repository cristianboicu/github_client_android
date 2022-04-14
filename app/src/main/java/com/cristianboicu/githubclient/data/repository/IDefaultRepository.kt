package com.cristianboicu.githubclient.data.repository

import androidx.lifecycle.LiveData
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.utils.Result
import com.cristianboicu.githubclient.utils.Status

interface IDefaultRepository {
    suspend fun getUser(): Result<User>

    suspend fun fetchUser(username: String): Result<User>

    suspend fun refreshUser(username: String): Status

    suspend fun getRepositoryById(id: Long): Result<DbGhRepository>

    fun observeRepositories(): LiveData<Result<List<DbGhRepository>>>

    suspend fun refreshRepositories(username: String)
}