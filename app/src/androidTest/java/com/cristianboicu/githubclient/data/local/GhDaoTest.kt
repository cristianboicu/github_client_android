package com.cristianboicu.githubclient.data.local

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.filters.SmallTest
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.data.repository.FakeAndroidDefaultRepository
import com.cristianboicu.githubclient.data.repository.IDefaultRepository
import com.cristianboicu.githubclient.di.AppModule
import com.cristianboicu.githubclient.getOrAwaitValue
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
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
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@UninstallModules(AppModule::class)
@HiltAndroidTest
class GhDaoTest {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class TestAppModule {
        @Binds
        abstract fun bindDefaultRepository(
            defaultRepository: FakeAndroidDefaultRepository,
        ): IDefaultRepository

        companion object {
            @Singleton
            @Provides
            fun provideDefaultRepository(
            ): FakeAndroidDefaultRepository =
                FakeAndroidDefaultRepository(mutableListOf(), mutableListOf(), null)

            @Provides
            fun provideDao(db: GhDatabase): GhDao = db.getGhDao()

            @Provides
            fun provideInMemoryDb(@ApplicationContext context: Context): GhDatabase {
                Log.d("TEST", "in memory db")
                return Room.inMemoryDatabaseBuilder(
                    context,
                    GhDatabase::class.java
                ).allowMainThreadQueries().build()
            }
        }
    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: GhDatabase

    @Inject
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