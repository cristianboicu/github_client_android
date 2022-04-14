package com.cristianboicu.githubclient.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cristianboicu.githubclient.R
import com.cristianboicu.githubclient.databinding.FragmentLoginBinding
import com.cristianboicu.githubclient.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.navigateToRepositories.observe(viewLifecycleOwner, EventObserver {
            this.findNavController().navigate(
                LoginFragmentDirections.showRepositories()
            )
        })

        viewModel.toastError.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, R.string.error_while_fetching_data, Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

}