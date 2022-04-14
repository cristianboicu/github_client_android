package com.cristianboicu.githubclient.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.data.repository.DefaultRepository
import com.cristianboicu.githubclient.utils.Event
import com.cristianboicu.githubclient.utils.Result
import com.cristianboicu.githubclient.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val defaultRepository: DefaultRepository) :
    ViewModel() {

    private val status = MutableLiveData<Status>()

    private val _toastError = MutableLiveData<Event<Unit>>()
    val toastError = _toastError

    private val _navigateToRepositories = MutableLiveData<Event<Unit>>()
    val navigateToRepositories = _navigateToRepositories

    fun showRepositoriesTriggered(inputUsername: String) {
        var localUser: User? = null

        viewModelScope.launch {
            val res = defaultRepository.getUser()
            if (res is Result.Success) {
                localUser = res.data
            }
            refreshUserData(inputUsername)
            status.value?.let {
                checkStatus(it, localUser, inputUsername)
            }
        }
    }

    private suspend fun refreshUserData(inputUsername: String) {
        if (inputUsername.isEmpty()) {
            return
        }
        status.value = Status.LOADING
        status.value = defaultRepository.refreshUser(inputUsername)
    }

    fun checkStatus(status: Status, localUser: User?, userInput: String?) {
        if (status == Status.ERROR) {
            _toastError.value = Event(Unit)
            if (checkIfUserStoredInDb(localUser, userInput)) {
                navigateToRepositories()
            }
        } else if (status == Status.SUCCESS) {
            navigateToRepositories()
        }
    }

    private fun checkIfUserStoredInDb(localUser: User?, userInput: String?): Boolean {
        localUser?.let {
            return it.login == userInput
        }
        return false
    }

    private fun navigateToRepositories() {
        _navigateToRepositories.value = Event(Unit)
    }
}