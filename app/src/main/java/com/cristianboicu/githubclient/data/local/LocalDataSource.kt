package com.cristianboicu.githubclient.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.utils.Result
import com.cristianboicu.githubclient.utils.Result.Error
import com.cristianboicu.githubclient.utils.Result.Success
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val ghDao: GhDao) : ILocalDataSource {

    override suspend fun getUser(): Result<User> {
        return try {
            val result = ghDao.getUser()
            result?.let {
                Success(it)
            } ?: Error(Exception("No such user"))
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun saveUser(user: User) {
        ghDao.saveUser(user)
    }

    override suspend fun deleteUser() {
        ghDao.deleteUser()
    }

    override suspend fun deleteRepositories() {
        return ghDao.deleteRepositories()
    }

    override suspend fun saveRepositories(repositories: List<DbGhRepository>) {
        return ghDao.saveRepositories(*repositories.toTypedArray())
    }

    override fun observeRepositories(): LiveData<Result<List<DbGhRepository>>> {
        return ghDao.observeRepositories().map {
            Success(it)
        }
    }

    override suspend fun getRepositoryById(id: Long): Result<DbGhRepository> {
        return try {
            val result = ghDao.getRepositoryById(id)
            result?.let {
                Success(it)
            } ?: Error(Exception("No such repository"))
        } catch (e: Exception) {
            Error(e)
        }
    }
}