package com.cristianboicu.githubclient.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User constructor(
    @PrimaryKey
    val id: Int,
    val login: String,
    val avatar_url: String,
    val html_url: String,
    val name: String?,
    val public_repos: Int,
)
