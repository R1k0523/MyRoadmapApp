package ru.boringowl.myroadmapapp.data.room.converters

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ru.boringowl.myroadmapapp.model.BookInfo
import ru.boringowl.myroadmapapp.model.UserRole

object RoomConverters {
    private val gson = GsonBuilder().setLenient().create()

    @TypeConverter
    fun stringToRole(name: String): UserRole = UserRole.valueOf(name)
    @TypeConverter
    fun roleToString(role: UserRole): String = role.name
    @TypeConverter
    fun stringToBooksList(data: String): List<BookInfo> =
        gson.fromJson(data, object : TypeToken<List<BookInfo?>?>() {}.type)
    @TypeConverter
    fun listOfBooksToString(books: List<BookInfo>): String = gson.toJson(books)
}