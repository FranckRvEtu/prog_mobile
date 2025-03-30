package com.example.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import com.example.persistence.dao.PlanningDao
import com.example.persistence.dao.UserDao
import com.example.persistence.entities.DailyPlanning
import com.example.persistence.entities.User
import kotlinx.coroutines.flow.Flow


@Database(entities = [User::class, DailyPlanning::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun planningDao(): PlanningDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_registration_db"
                ).build().also { instance = it }
            }
        }
    }
}