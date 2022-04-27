package com.cristianboicu.githubclient.ui.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.cristianboicu.githubclient.MainCoroutineAndroidRule
import com.cristianboicu.githubclient.R
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.repository.FakeAndroidDefaultRepository
import com.cristianboicu.githubclient.data.repository.IDefaultRepository
import com.cristianboicu.githubclient.di.AppModule
import com.cristianboicu.githubclient.launchFragmentInHiltContainer
import com.cristianboicu.githubclient.utils.Result
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton

@MediumTest
@HiltAndroidTest
@UninstallModules(AppModule::class)
@ExperimentalCoroutinesApi
class RepositoriesFragmentTest {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class TestRepositoryModule {
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
        }
    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineAndroidRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testRepositories = mutableListOf<DbGhRepository>(
        DbGhRepository(0, "Name0", "", true, "",
            "", "", "", "", 0),
        DbGhRepository(1, "Name1", "", false, "",
            "", "", "", "", 0),
    )

    @Inject
    lateinit var repository: FakeAndroidDefaultRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun cleanUp() {
        repository = FakeAndroidDefaultRepository(mutableListOf(), mutableListOf(), null)
    }

    @Test
    fun clickOnRepositoryItem_NavigateToDetails() = mainCoroutineRule.runBlockingTest {
        val navController = Mockito.mock(NavController::class.java)
        repository.refreshUser("username")
        repository.setUpRepositories(testRepositories)

        launchFragmentInHiltContainer<RepositoriesFragment>() {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.rv_repositories))
            .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                ViewMatchers.hasDescendant(ViewMatchers.withText("Name0")), ViewActions.click()))

        Mockito.verify(navController).navigate(
            RepositoriesFragmentDirections.showDetails(0)
        )
    }

    @Test
    fun checkUserInfoDisplayed() = runTest {
        val newUser = "newUser"
        repository.refreshUser(newUser)

        launchFragmentInHiltContainer<RepositoriesFragment>() {
        }

        val fetchedUser = repository.fetchUser(newUser) as Result.Success
        val publicRepos = "Public repositories: " + fetchedUser.data.public_repos

        onView(withId(R.id.tv_username))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_username))
            .check(ViewAssertions.matches(ViewMatchers.withText(newUser)))
        onView(withId(R.id.tv_total_repos))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_total_repos))
            .check(ViewAssertions.matches(ViewMatchers.withText(publicRepos)))
    }

}