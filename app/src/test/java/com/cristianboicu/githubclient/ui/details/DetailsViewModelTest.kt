package com.cristianboicu.githubclient.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cristianboicu.githubclient.MainCoroutineRule
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.GhRepository
import com.cristianboicu.githubclient.data.repository.FakeDefaultRepository
import com.cristianboicu.githubclient.getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val localRepository =
        DbGhRepository(1, "name", "", false, "", "", "", "", "", 0)
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var defaultRepository: FakeDefaultRepository

    @Before
    fun setUp() {
        defaultRepository = FakeDefaultRepository(mutableListOf(localRepository),
            mutableListOf(),
            null)
        detailsViewModel = DetailsViewModel(defaultRepository)
    }

    @Test
    fun start_getRequestedRepositoryByIdAsDomainModel() {
        detailsViewModel.start(1)

        val requestedRepository =
            detailsViewModel.userRepository.getOrAwaitValueTest() as GhRepository

        assertThat(requestedRepository.id, `is`(localRepository.id))
        assertThat(requestedRepository.name, `is`(localRepository.name))
    }
}