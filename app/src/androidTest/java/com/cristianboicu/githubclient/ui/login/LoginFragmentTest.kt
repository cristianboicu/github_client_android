package com.cristianboicu.githubclient.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.cristianboicu.githubclient.MainCoroutineAndroidRule
import com.cristianboicu.githubclient.R
import com.cristianboicu.githubclient.data.repository.FakeAndroidDefaultRepository
import com.cristianboicu.githubclient.data.repository.IDefaultRepository
import com.cristianboicu.githubclient.di.AppModule
import com.cristianboicu.githubclient.launchFragmentInHiltContainer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject
import javax.inject.Singleton

@MediumTest
@HiltAndroidTest
@UninstallModules(AppModule::class)
@ExperimentalCoroutinesApi
class LoginFragmentTest {

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

    @Inject
    lateinit var repository: FakeAndroidDefaultRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun clickShowRepositoriesButton() = runBlocking {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<LoginFragment>() {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.et_username)).perform(ViewActions.typeText("defunkt"))
        onView(withId(R.id.btn_navigate_to_repos)).perform(ViewActions.click())

        verify(navController).navigate(
            LoginFragmentDirections.showRepositories()
        )
    }

    @Test
    fun clickShowRepositoriesButton_ensureButtonDisabledWhileLoading() = runTest {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<LoginFragment>() {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.et_username)).perform(ViewActions.typeText("defunkt"))
        mainCoroutineRule.pauseDispatcher()
        onView(withId(R.id.btn_navigate_to_repos)).perform(ViewActions.click())
        onView(withId(R.id.btn_navigate_to_repos)).check(matches(not(isEnabled())))
    }
}