package com.example.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.jetbrains.annotations.NotNull
import java.time.LocalDate
import java.time.LocalTime

// User Entity
@Entity(tableName = "users")
data class User(
    @PrimaryKey val login: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val phoneNumber: String,
    val email: String,
    val interests: List<String>
)
