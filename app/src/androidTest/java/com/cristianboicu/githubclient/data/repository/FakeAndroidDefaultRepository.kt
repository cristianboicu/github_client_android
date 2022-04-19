package com.cristianboicu.githubclient.data.repository

import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User

class FakeAndroidDefaultRepository constructor(
    private val localRepositories: MutableList<DbGhRepository>,
    private val remoteRepositories: MutableList<DbGhRepository>,
    private var user: User?,
) {
}