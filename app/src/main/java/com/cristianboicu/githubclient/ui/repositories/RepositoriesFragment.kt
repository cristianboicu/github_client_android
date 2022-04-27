package com.cristianboicu.githubclient.ui.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cristianboicu.githubclient.R
import com.cristianboicu.githubclient.databinding.FragmentRepositoriesBinding
import com.cristianboicu.githubclient.ui.adapter.RepositoriesAdapter
import com.cristianboicu.githubclient.ui.adapter.RepositoryListener
import com.cristianboicu.githubclient.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesFragment : Fragment() {

    lateinit var viewModel: RepositoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding: FragmentRepositoriesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_repositories, container, false)

        viewModel = ViewModelProvider(this)[RepositoriesViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = RepositoriesAdapter(RepositoryListener {
            viewModel.onRepositoryClicked(it)
        })

        binding.rvRepositories.adapter = adapter

        viewModel.repositories.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.navigateToDetails.observe(viewLifecycleOwner, EventObserver {
            this.findNavController().navigate(
                RepositoriesFragmentDirections.showDetails(it)
            )
        })

        return binding.root
    }

}