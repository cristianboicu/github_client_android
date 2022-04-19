package com.cristianboicu.githubclient.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class GhDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("inMemoryDb")
    lateinit var database: GhDatabase

    @Inject
    @Named("inMemoryDao")
    lateinit var dao: GhDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertUser() {
        runTest {
            val user = User(0, "login", "", "", "", 1)
            dao.saveUser(user)

            val dbUser = dao.getUser()
            assertThat(dbUser, equalTo(user))
        }
    }

    @Test
    fun deleteUser() {
        runTest {
            val user = User(0, "login", "", "", "", 1)
            dao.saveUser(user)
            dao.deleteUser()

            val dbUser = dao.getUser()
            assertThat(dbUser, equalTo(null))
        }
    }

    @Test
    fun observeUser() {
        runTest {
            val user = User(0, "login", "", "", "", 1)
            dao.saveUser(user)

            val dbUser = dao.observeUser().getOrAwaitValue()
            assertThat(dbUser, equalTo(user))
        }
    }

    @Test
    fun insertRepositories() {
        runTest {
            val repository1 =
                DbGhRepository(1, "rep1", "", true, "", "", "", "", "", 0)
            val repository2 =
                DbGhRepository(2, "rep2", "", true, "", "", "", "", "", 0)
            val repository3 =
                DbGhRepository(3, "rep3", "", true, "", "", "", "", "", 0)
            dao.saveRepositories(repository1, repository2, repository3)

            val dbRepositories = dao.observeRepositories().getOrAwaitValue()
            assertThat(dbRepositories.count(), `is`(3))
        }
    }

    @Test
    fun deleteRepositories() {
        runTest {
            val repository1 =
                DbGhRepository(1, "rep1", "", true, "", "", "", "", "", 0)
            val repository2 =
                DbGhRepository(2, "rep2", "", true, "", "", "", "", "", 0)
            val repository3 =
                DbGhRepository(3, "rep3", "", true, "", "", "", "", "", 0)
            dao.saveRepositories(repository1, repository2, repository3)
            dao.deleteRepositories()

            val dbRepositories = dao.observeRepositories().getOrAwaitValue()
            assertThat(dbRepositories.count(), `is`(0))
        }
    }

    @Test
    fun getRepositoryById_existingId_returnsRepository() {
        runTest {
            val repository1 =
                DbGhRepository(1, "rep1", "", true, "", "", "", "", "", 0)
            val repository2 =
                DbGhRepository(2, "rep2", "", true, "", "", "", "", "", 0)
            val repository3 =
                DbGhRepository(3, "rep3", "", true, "", "", "", "", "", 0)
            dao.saveRepositories(repository1, repository2, repository3)

            val dbRepository = dao.getRepositoryById(3)
            assertThat(dbRepository, equalTo(repository3))
        }
    }

    @Test
    fun getRepositoryById_notExistingId_returnsNull() {
        runTest {
            val repository1 =
                DbGhRepository(1, "rep1", "", true, "", "", "", "", "", 0)
            val repository2 =
                DbGhRepository(2, "rep2", "", true, "", "", "", "", "", 0)
            val repository3 =
                DbGhRepository(3, "rep3", "", true, "", "", "", "", "", 0)
            dao.saveRepositories(repository1, repository2, repository3)

            val dbRepository = dao.getRepositoryById(4)
            assertThat(dbRepository, equalTo(null))
        }
    }

}