package com.cristianboicu.githubclient.data.remote

import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import kotlinx.coroutines.delay
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

class FakeApiService(
    private var repositories: List<DbGhRepository>,
    private var user: User,
) : ApiService {

    private var _shouldReturnError = false

    fun shouldReturnError(value: Boolean) {
        _shouldReturnError = value
    }

    override suspend fun getUserData(username: String): Response<User> {
        delay(1000)
        return when {
            _shouldReturnError -> {
                Response.error(500, ResponseBody.create(MediaType.get("text"), "error"))
            }
            username == user.login -> {
                Response.success(user)
            }
            else -> {
                Response.error(404, ResponseBody.create(MediaType.get("text"), "error"))
            }
        }
    }

    override suspend fun getUserRepositories(username: String): Response<List<DbGhRepository>> {
        delay(1000)
        return when {
            _shouldReturnError -> {
                Response.error(500, ResponseBody.create(MediaType.get("text"), "error"))
            }
            username == user.login -> {
                Response.success(repositories)
            }
            else -> {
                Response.error(404, ResponseBody.create(MediaType.get("text"), "error"))
            }
        }
    }
}