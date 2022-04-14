package com.cristianboicu.githubclient.utils

import android.text.util.Linkify
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.cristianboicu.githubclient.R
import com.cristianboicu.githubclient.data.model.GhRepository
import com.cristianboicu.githubclient.data.model.User

@BindingAdapter("userAvatarUrl")
fun setAvatarUrl(imageView: ImageView, user: User?) {
    Glide.with(imageView.context).load(user?.avatar_url).into(imageView)
}

@BindingAdapter("userName")
fun TextView.setUserName(item: User?) {
    item?.let {
        text = it.name
    }
}

@BindingAdapter("userTotalRepositories")
fun TextView.setUserTotalRepositories(item: User?) {
    item?.let {
        text = String.format(resources.getString(R.string.public_repositories), it.public_repos)
    }
}

@BindingAdapter("setRepositoryTitle")
fun TextView.setRepositoryTitle(item: GhRepository?) {
    item?.let {
        text = it.name
    }
}

@BindingAdapter("setRepositoryContent")
fun TextView.setRepositoryContent(repository: GhRepository?) {
    repository?.let {
        text = String.format(resources.getString(R.string.repository_content),
            it.description,
            it.html_url,
            it.created_at,
            it.updated_at,
            it.language,
            it.open_issues_count)
        it.html_url?.let { it1 -> Linkify.addLinks(this, it1.toPattern(), it.html_url) };
    }
}