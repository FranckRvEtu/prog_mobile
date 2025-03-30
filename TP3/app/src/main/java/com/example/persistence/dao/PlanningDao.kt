package com.example.persistence.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.persistence.entities.DailyPlanning
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PlanningDao {

    @Insert
    suspend fun insertPlanning(planning: DailyPlanning)

    @Query("SELECT * FROM daily_planning WHERE userId = :userId")
    fun getPlanningForUser(userId: String): Flow<List<DailyPlanning>>

    @Query("SELECT * FROM daily_planning WHERE userId = :userId AND date = :date")
    suspend fun getTodayPlanningForUser(userId: String, date: Long): DailyPlanning

    @Query("SELECT COUNT(*) FROM daily_planning WHERE userId = :userId AND date = :date")
    suspend fun checkPlanningExists(userId: String, date: Long): Int

}