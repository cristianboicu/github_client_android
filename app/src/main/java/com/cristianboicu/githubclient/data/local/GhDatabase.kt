package com.cristianboicu.githubclient.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User

@Database(entities = [User::class, DbGhRepository::class], version = 1)
abstract class GhDatabase : RoomDatabase() {
    abstract fun getGhDao(): GhDao
}