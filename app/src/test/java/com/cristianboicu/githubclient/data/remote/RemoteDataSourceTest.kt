package com.cristianboicu.githubclient.data.remote

import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.utils.Result
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteDataSourceTest {

    private val remoteRepository0 =
        DbGhRepository(0, "", "", false, "", "", "", "", "", 0)
    private val remoteRepository1 =
        DbGhRepository(1, "", "", false, "", "", "", "", "", 0)
    private val remoteRepository2 =
        DbGhRepository(2, "", "", false, "", "", "", "", "", 0)

    private val remoteRepositories =
        listOf<DbGhRepository>(remoteRepository0, remoteRepository1, remoteRepository2)


    private val username = "username"
    private val remoteUser: User = User(2, username, "", "", "", 3)

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var fakeApiService: FakeApiService

    @Before
    fun setUp() {
        fakeApiService = FakeApiService(remoteRepositories, remoteUser)
        remoteDataSource = RemoteDataSource(fakeApiService)
    }

    @Test
    fun getUserData_returnsUser() = runTest {
        val user = remoteDataSource.getUser(username)

        assertTrue(user is Result.Success)
    }

    @Test
    fun getUserData_returnsError() = runTest {
        fakeApiService.shouldReturnError(true)
        val user = remoteDataSource.getUser(username)

        assertTrue(user is Result.Error)
    }

    @Test
    fun getUserData_notExistingUser_returnsError() = runTest {
        val user = remoteDataSource.getUser("usernam")

        assertTrue(user is Result.Error)
    }

    @Test
    fun getUserRepositories_returnsRepositories() = runTest {
        val repositories = remoteDataSource.getUserRepositories(username)

        assertTrue(repositories is Result.Success)
    }

    @Test
    fun getUserRepositories_returnsError() = runBlocking {
        fakeApiService.shouldReturnError(true)

        val repositories = remoteDataSource.getUserRepositories(username)
        assertTrue(repositories is Result.Error)
    }

    @Test
    fun getUserRepositories_notExistingUser_returnsError() = runTest {
        val repositories = remoteDataSource.getUserRepositories("usernam")

        assertTrue(repositories is Result.Error)
    }
}