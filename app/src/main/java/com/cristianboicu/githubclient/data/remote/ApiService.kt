package com.cristianboicu.githubclient.data.remote

import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/users/{username}")
    suspend fun getUserData(@Path("username") username: String): Response<User>

    @GET("/users/{username}/repos")
    suspend fun getUserRepositories(@Path("username") username: String): Response<List<DbGhRepository>>
}