package com.example.persistence.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.persistence.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE login = :login")
    suspend fun getUserByLogin(login: String): User?

    @Query("SELECT COUNT(*) FROM users WHERE login = :login")
    suspend fun checkLoginExists(login: String): Int

    @Query("SELECT * FROM users WHERE login = :login AND password = :password")
    suspend fun checkLoginInfos(login: String, password : String) : User?

}