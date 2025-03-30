package com.example.persistence.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime


@Entity(tableName = "daily_planning",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["login"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class DailyPlanning(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val date: Long,
    val slot1Activity: String, // 08h-10h
    val slot2Activity: String, // 10h-12h
    val slot3Activity: String, // 14h-16h
    val slot4Activity: String  // 16h-18h
)