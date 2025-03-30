package com.example.persistence
import androidx.room.TypeConverter
import java.time.LocalDate

object Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long = date.toEpochDay()

    @TypeConverter
    fun toLocalDate(epochDay: Long): LocalDate =
        epochDay.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun fromInterestsList(interests: List<String>): String =
        interests.joinToString(",")

    @TypeConverter
    fun toInterestsList(interestsString: String): List<String> =
        interestsString.split(",")
}