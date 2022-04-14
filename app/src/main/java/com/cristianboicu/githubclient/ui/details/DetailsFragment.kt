package com.cristianboicu.githubclient.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cristianboicu.githubclient.R
import com.cristianboicu.githubclient.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding: FragmentDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val repositoryId = DetailsFragmentArgs.fromBundle(requireArguments()).id
        viewModel.start(repositoryId)

        viewModel.userRepository.observe(viewLifecycleOwner) {
            it?.let {
                binding.repository = it
            }
        }

        return binding.root
    }
}