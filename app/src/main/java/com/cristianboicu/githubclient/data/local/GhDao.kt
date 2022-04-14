package com.cristianboicu.githubclient.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristianboicu.githubclient.data.model.DbGhRepository
import com.cristianboicu.githubclient.data.model.User

@Dao
interface GhDao {
    @Query("select * from user")
    suspend fun getUser(): User?

    @Query("select * from user")
    fun observeUser(): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Query("delete from user")
    suspend fun deleteUser()

    @Query("delete from repositories")
    suspend fun deleteRepositories()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRepositories(vararg repositoryDatabase: DbGhRepository)

    @Query("select * from repositories")
    fun observeRepositories(): LiveData<List<DbGhRepository>>

    @Query("select * from repositories where id=:id")
    suspend fun getRepositoryById(id: Long): DbGhRepository?
}