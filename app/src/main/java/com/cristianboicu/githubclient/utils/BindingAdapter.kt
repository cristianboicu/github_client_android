package com.cristianboicu.githubclient.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.cristianboicu.githubclient.R
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