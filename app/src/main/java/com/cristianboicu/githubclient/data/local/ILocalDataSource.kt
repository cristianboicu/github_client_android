package com.cristianboicu.githubclient.data.local

import androidx.lifecycle.LiveData
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.utils.Result


interface ILocalDataSource {
    suspend fun getUser(): Result<User>

    suspend fun saveUser(user: User)

    suspend fun deleteUser()

    suspend fun deleteRepositories()

    suspend fun saveRepositories(repositories: List<DbGhRepository>)

    fun observeRepositories(): LiveData<Result<List<DbGhRepository>>>

    suspend fun getRepositoryById(id: Long): Result<DbGhRepository>
}