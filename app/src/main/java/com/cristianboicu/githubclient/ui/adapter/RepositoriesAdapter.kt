package com.cristianboicu.githubclient.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cristianboicu.githubclient.data.model.GhRepository
import com.cristianboicu.githubclient.databinding.RepositoryItemBinding

class RepositoriesAdapter(private val clickListener: RepositoryListener) :
    ListAdapter<GhRepository, ReposViewHolder>(ReposDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        return ReposViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        val repository = getItem(position)
        holder.bind(repository, clickListener)
    }
}

class ReposViewHolder private constructor(private val binding: RepositoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: GhRepository,
        clickListener: RepositoryListener,
    ) {
        binding.repository = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ReposViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RepositoryItemBinding.inflate(layoutInflater, parent, false)
            return ReposViewHolder(binding)
        }
    }
}

class ReposDiffCallback : DiffUtil.ItemCallback<GhRepository>() {
    override fun areItemsTheSame(oldItem: GhRepository, newItem: GhRepository): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GhRepository, newItem: GhRepository): Boolean {
        return oldItem == newItem
    }
}

class RepositoryListener(val clickListener: (repositoryId: Long) -> Unit) {
    fun onClick(repositoryDatabase: GhRepository) = clickListener(repositoryDatabase.id)
}