package com.cristianboicu.githubclient.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cristianboicu.githubclient.MainCoroutineRule
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.data.repository.FakeDefaultRepository
import com.cristianboicu.githubclient.getOrAwaitValueTest
import com.cristianboicu.githubclient.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var loginViewModel: LoginViewModel
    private val localUser = User(1, "username", "", "", "", 2)
    private var userInput = "username"
    private lateinit var defaultRepository: FakeDefaultRepository

    @Before
    fun setUp() {
        defaultRepository = FakeDefaultRepository(mutableListOf(),
            mutableListOf(),
            localUser)
        loginViewModel = LoginViewModel(defaultRepository)
    }

    @Test
    fun checkStatus_errorStatus_SameUsername_setsNavigateToRepositoriesEvent() {
        val status = Status.ERROR

        loginViewModel.checkStatus(status, localUser, userInput)
        val value = loginViewModel.navigateToRepositories.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun checkStatus_successStatus_setNavigateToRepositoriesEvent() {
        val status = Status.SUCCESS

        loginViewModel.checkStatus(status, localUser, userInput)
        val value = loginViewModel.navigateToRepositories.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun checkStatus_errorStatus_setToastErrorEvent() {
        val status = Status.ERROR
        userInput = "different_username"

        loginViewModel.checkStatus(status, localUser, userInput)
        val value = loginViewModel.toastError.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun showRepositoriesTriggered_StatusLoading_DisablesButton(){
        mainCoroutineRule.pauseDispatcher()

        loginViewModel.showRepositoriesTriggered("username")

        assertThat(loginViewModel.status.getOrAwaitValueTest(), `is`(Status.LOADING))
        assertThat(loginViewModel.buttonEnabled.getOrAwaitValueTest(), `is`(false))
    }
}