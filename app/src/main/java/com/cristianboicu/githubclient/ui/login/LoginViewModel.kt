package com.cristianboicu.githubclient.ui.login

import androidx.lifecycle.ViewModel
import com.cristianboicu.githubclient.data.repository.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val defaultRepository: DefaultRepository): ViewModel() {

    fun showRepositoriesTriggered(username: String){

    }
}