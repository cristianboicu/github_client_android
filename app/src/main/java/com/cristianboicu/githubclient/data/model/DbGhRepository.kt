package com.cristianboicu.githubclient.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cristianboicu.githubclient.utils.convertBooleanToPrivateOrPublic
import com.cristianboicu.githubclient.utils.formatDate

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

fun List<DbGhRepository>.asDomainModel(): List<GhRepository> {
    return map {
        it.asDomainModel()
    }
}

fun DbGhRepository.asDomainModel(): GhRepository {
    return let {
        GhRepository(
            id = it.id,
            name = it.name,
            full_name = it.full_name,
            privateR = convertBooleanToPrivateOrPublic(it.privateR),
            html_url = it.html_url,
            description = it.description,
            created_at = formatDate(it.created_at),
            updated_at = formatDate(it.updated_at),
            language = it.language,
            open_issues_count = it.open_issues_count.toString()
        )
    }
}