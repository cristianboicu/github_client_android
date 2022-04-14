package com.cristianboicu.githubclient.data.remote

import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.utils.Result
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) : IRemoteDataSource {

    override suspend fun getUser(username: String): Result<User> {
        return try {
            val userDataResponse = apiService.getUserData(username)
            if (userDataResponse.isSuccessful) {
                userDataResponse.body()?.let {
                    Result.Success(it)
                } ?: Result.Error(Exception("Unknown error"))
            } else {
                Result.Error(Exception("Unknown error"))
            }
        } catch (e: Exception) {
            Result.Error(Exception("Couldn't reach server. Check your internet connection!"))
        }
    }

    override suspend fun getUserRepositories(username: String): Result<List<DbGhRepository>> {
        return try {
            val userDataResponse = apiService.getUserRepositories(username)
            if (userDataResponse.isSuccessful) {
                userDataResponse.body()?.let {
                    Result.Success(it)
                } ?: Result.Error(Exception("Unknown error"))
            } else {
                Result.Error(Exception("Unknown error"))
            }
        } catch (e: Exception) {
            Result.Error(Exception("Couldn't reach server. Check your internet connection!"))
        }
    }
}