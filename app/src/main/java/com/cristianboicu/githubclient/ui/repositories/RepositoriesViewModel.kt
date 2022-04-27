package com.cristianboicu.githubclient.ui.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianboicu.githubclient.data.model.User
import com.cristianboicu.githubclient.data.model.asDomainModel
import com.cristianboicu.githubclient.data.repository.IDefaultRepository
import com.cristianboicu.githubclient.utils.Event
import com.cristianboicu.githubclient.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(private val defaultRepository: IDefaultRepository) :
    ViewModel() {

    val user = MutableLiveData<User>()
    val repositories = Transformations.map(defaultRepository.observeRepositories()) {
        if (it is Result.Success) {
            it.data.asDomainModel()
        } else {
            emptyList()
        }
    }

    private val _navigateToDetails = MutableLiveData<Event<Long>>()
    val navigateToDetails = _navigateToDetails

    init {
        viewModelScope.launch {
            defaultRepository.getUser().let { result ->
                if (result is Result.Success) {
                    result.data.let {
                        user.value = it
                    }
                    defaultRepository.refreshRepositories(user.value!!.login)
                }
            }
        }
    }

    fun onRepositoryClicked(it: Long?) {
        it?.let {
            _navigateToDetails.value = Event(it)
        }
    }

}