package com.cristianboicu.githubclient.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cristianboicu.githubclient.MainCoroutineRule
import com.cristianboicu.githubclient.data.local.FakeLocalDataSource
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.data.remote.FakeRemoteDataSource
import com.cristianboicu.githubclient.getOrAwaitValueTest
import com.cristianboicu.githubclient.utils.Result
import com.cristianboicu.githubclient.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var defaultRepository: DefaultRepository
    private lateinit var remoteDataSource: FakeRemoteDataSource
    private lateinit var localDataSource: FakeLocalDataSource

    private val username = "username"
    private val remoteRepository0 =
        DbGhRepository(0, "", "", false, "", "", "", "", "", 0)
    private val remoteRepository1 =
        DbGhRepository(1, "", "", false, "", "", "", "", "", 0)
    private val remoteRepository2 =
        DbGhRepository(2, "", "", false, "", "", "", "", "", 0)

    private val remoteRepositories =
        listOf(remoteRepository0, remoteRepository1, remoteRepository2)

    private val localUser: User? = null
    private val localRepositories = mutableListOf<DbGhRepository>()

    @Before
    fun setUpRepository() {
        remoteDataSource = FakeRemoteDataSource(remoteRepositories)
        localDataSource = FakeLocalDataSource(localRepositories, localUser)

        defaultRepository = DefaultRepository(
            localDataSource, remoteDataSource
        )
    }

    @Test
    fun refreshUser_sameUserStoredLocally() = runTest {
        val status = defaultRepository.refreshUser(username)

        val localUser = defaultRepository.getUser().let {
            if (it is Result.Success)
                it.data
        }
        val remoteUser = defaultRepository.fetchUser(username).let {
            if (it is Result.Success)
                it.data
        }

        assertThat(status, `is`(Status.SUCCESS))
        assertThat(localUser, samePropertyValuesAs(remoteUser))
    }

    @Test
    fun refreshRepositories_sameRepositoriesStoredLocally() = runTest {
        defaultRepository.refreshRepositories(username)

        val localRepos = defaultRepository.observeRepositories().getOrAwaitValueTest().let { it ->
            if (it is Result.Success) {
                it.data
            } else {
                emptyList()
            }
        }

        assertThat(remoteRepositories, IsEqual(localRepos))
    }

    @Test
    fun deleteAll_noDataStored() = runTest {
        defaultRepository.refreshUser(username)
        defaultRepository.refreshRepositories(username)

        defaultRepository.deleteAll()

        val localRepos = defaultRepository.observeRepositories().getOrAwaitValueTest().let {
            if (it is Result.Success) {
                it.data
            } else {
                emptyList()
            }
        }

        val localUser = defaultRepository.getUser().let {
            if (it is Result.Success) {
                it.data
            } else {
                null
            }
        }

        assertThat(localRepos, `is`(emptyList()))
        assertThat(localUser, `is`(nullValue()))
    }
}