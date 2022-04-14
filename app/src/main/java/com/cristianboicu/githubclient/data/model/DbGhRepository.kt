package com.cristianboicu.githubclient.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class DbGhRepository(
    @PrimaryKey
    val id: Long,
    val name: String,
    val full_name: String?,
    @ColumnInfo(name = "private")
    val privateR: Boolean,
    val html_url: String?,
    val description: String?,
    val created_at: String,
    val updated_at: String,
    val language: String?,
    val open_issues_count: Int
)
