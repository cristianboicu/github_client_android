package com.cristianboicu.githubclient.ui.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cristianboicu.githubclient.MainCoroutineRule
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.data.repository.FakeDefaultRepository
import com.cristianboicu.githubclient.getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RepositoriesViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repositoriesViewModel: RepositoriesViewModel
    private lateinit var defaultRepository: FakeDefaultRepository
    private val user = User(0,"login","","","",0)

    @Before
    fun setUp() {
        defaultRepository = FakeDefaultRepository(mutableListOf(),
            mutableListOf(),
            user)
        repositoriesViewModel = RepositoriesViewModel(defaultRepository)
    }

    @Test
    fun onRepositoryClicked_setsNavigationEvent() {
        repositoriesViewModel.onRepositoryClicked(1)

        val value = repositoriesViewModel.navigateToDetails.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled(), Matchers.not(Matchers.nullValue()))
    }

    @Test
    fun onInitViewModel_userNotNull() {
        assertThat(repositoriesViewModel.user.getOrAwaitValueTest(), Matchers.not(Matchers.nullValue()))
    }
}