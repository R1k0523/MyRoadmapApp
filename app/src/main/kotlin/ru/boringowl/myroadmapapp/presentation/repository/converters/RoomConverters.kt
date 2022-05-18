package ru.boringowl.myroadmapapp.presentation.repository.converters

import androidx.room.TypeConverter
import ru.boringowl.myroadmapapp.model.UserRole
import java.time.LocalDateTime

object RoomConverters {
    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? = dateString?.let { LocalDateTime.parse(it) }
    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? = date?.toString()
    @TypeConverter
    fun stringToRole(name: String): UserRole = UserRole.valueOf(name)
    @TypeConverter
    fun roleToString(role: UserRole): String = role.name
}