package com.cristianboicu.githubclient.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianboicu.githubclient.data.model.GhRepository
import com.cristianboicu.githubclient.data.model.asDomainModel
import com.cristianboicu.githubclient.data.repository.DefaultRepository
import com.cristianboicu.githubclient.data.repository.IDefaultRepository
import com.cristianboicu.githubclient.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val defaultRepository: IDefaultRepository) :
    ViewModel() {

    private val _userRepository = MutableLiveData<GhRepository>()
    val userRepository: LiveData<GhRepository>
        get() = _userRepository

    fun start(id: Long) {
        viewModelScope.launch {
            val result = defaultRepository.getRepositoryById(id)
            if (result is Result.Success) {
                launch(Dispatchers.IO) {
                    _userRepository.postValue(result.data.asDomainModel())
                }
            }
        }
    }
}