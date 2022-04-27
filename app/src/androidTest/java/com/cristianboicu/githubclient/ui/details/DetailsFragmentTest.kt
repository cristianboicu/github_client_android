package com.cristianboicu.githubclient.ui.details

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.cristianboicu.githubclient.R
import com.cristianboicu.githubclient.data.model.DbGhRepository
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
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@MediumTest
@HiltAndroidTest
@UninstallModules(AppModule::class)
@ExperimentalCoroutinesApi
class DetailsFragmentTest {

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

//    @Test
//    fun selectedRepository_DisplayedInUi() = runTest {
//        repository.setUpRepositories(testRepositories)
//        val bundle = Bundle()
//        val id: Long = 1
//        bundle.putLong("id", id)
////        val bundle = DetailsFragmentArgs().toBundle()
//        launchFragmentInHiltContainer<DetailsFragment>(bundle) {
//        }
//
//        onView(withId(R.id.tv_repository_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//        onView(withId(R.id.tv_repository_title)).check(ViewAssertions.matches(ViewMatchers.withText(
//            "Name1")))
//    }
}