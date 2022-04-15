package com.cristianboicu.githubclient.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
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

    private val _status = MutableLiveData<Status>()

    private val _localUser = MutableLiveData<User?>()

    val buttonEnabled = _status.map {
        it != Status.LOADING
    }

    private val _toastError = MutableLiveData<Event<Unit>>()
    val toastError = _toastError

    private val _navigateToRepositories = MutableLiveData<Event<Unit>>()
    val navigateToRepositories = _navigateToRepositories


    init {
        _status.value = Status.ERROR
        getLocalUser()
    }

    private fun getLocalUser() {
        viewModelScope.launch {
            defaultRepository.getUser().let {
                if (it is Result.Success) {
                    _localUser.value = it.data
                } else {
                    _localUser.value = null
                }
            }
        }
    }

    fun showRepositoriesTriggered(inputUsername: String) {
        viewModelScope.launch {
            refreshUserData(inputUsername)
            checkStatus(_status.value, _localUser.value, inputUsername)
        }
    }

    private suspend fun refreshUserData(inputUsername: String) {
        if (inputUsername.isEmpty()) {
            return
        }
        _status.value = Status.LOADING
        _status.value = defaultRepository.refreshUser(inputUsername)
    }

    fun checkStatus(status: Status?, localUser: User?, userInput: String?) {
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