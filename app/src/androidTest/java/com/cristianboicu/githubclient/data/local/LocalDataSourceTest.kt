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
import com.cristianboicu.githubclient.utils.Result
import com.cristianboicu.githubclient.utils.succeeded
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
class LocalDataSourceTest {

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

    lateinit var localDataSource: LocalDataSource

    private val repository1 =
        DbGhRepository(1, "rep1", "", true, "", "", "", "", "", 0)
    private val repository2 =
        DbGhRepository(2, "rep2", "", true, "", "", "", "", "", 0)
    private val repository3 =
        DbGhRepository(3, "rep3", "", true, "", "", "", "", "", 0)
    private val user = User(11, "login11", "", "", "", 1)

    @Before
    fun setup() {
        hiltRule.inject()
        localDataSource = LocalDataSource(dao)
    }

    @Test
    fun getUser_existingUser_returnsSuccessAndSameUser() = runTest {
        localDataSource.saveUser(user)

        val result = localDataSource.getUser()

        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data, equalTo(user))
    }

    @Test
    fun getUser_noUserStored_returnsError() = runTest {
        val result = localDataSource.getUser()
        assertThat(result.succeeded, `is`(false))
    }

    @Test
    fun deleteUser_getUser_returnsError() = runTest {
        localDataSource.saveUser(user)
        localDataSource.deleteUser()

        val result = localDataSource.getUser()

        assertThat(result.succeeded, `is`(false))
    }

    @Test
    fun deleteRepositories_getRepositories_returnsEmptyList() = runTest {

        localDataSource.saveRepositories(listOf(repository1, repository2, repository3))
        localDataSource.deleteRepositories()

        val result = localDataSource.observeRepositories().getOrAwaitValue()

        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data, equalTo(emptyList()))
    }

    @Test
    fun saveRepositories_retrievesRepositories() = runTest {
        localDataSource.saveRepositories(listOf(repository1, repository2, repository3))

        val result = localDataSource.observeRepositories().getOrAwaitValue()

        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data, equalTo(listOf(repository1, repository2, repository3)))
    }

    @Test
    fun observeRepositories_returnsEmptyList() = runTest {
        val result = localDataSource.observeRepositories().getOrAwaitValue()

        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data, equalTo(emptyList()))
    }

    @Test
    fun getRepositoryById_existingRepository_returnsSuccess() = runTest {
        localDataSource.saveRepositories(listOf(repository1, repository2, repository3))

        val result = localDataSource.getRepositoryById(1)

        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data, equalTo(repository1))
    }

    @Test
    fun getRepositoryById_notExistingRepository_returnsError() = runTest {
        localDataSource.saveRepositories(listOf(repository1, repository2, repository3))

        val result = localDataSource.getRepositoryById(5)

        assertThat(result.succeeded, `is`(false))
        result as Result.Error
    }

    @After
    fun tearDown() = runTest {
        localDataSource.deleteUser()
        localDataSource.deleteRepositories()
        database.close()
    }
}