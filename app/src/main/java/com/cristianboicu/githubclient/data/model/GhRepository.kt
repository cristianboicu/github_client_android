package com.cristianboicu.githubclient.data.model

data class GhRepository constructor(
    val id: Long,
    val name: String,
    val full_name: String?,
    val privateR: String,
    val html_url: String?,
    val description: String?,
    val created_at: String,
    val updated_at: String,
    val language: String?,
    val open_issues_count: String,
)
