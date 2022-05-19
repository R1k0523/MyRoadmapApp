package ru.boringowl.myroadmapapp.presentation.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.boringowl.myroadmapapp.presentation.repository.room.converters.RoomConverters
import ru.boringowl.myroadmapapp.presentation.repository.room.dao.*
import ru.boringowl.myroadmapapp.presentation.repository.room.model.*


@Database(
    entities = [
        HackathonEntity::class,
        RouteEntity::class,
        SkillEntity::class,
        SkillTodoEntity::class,
        TodoEntity::class,
        UserEntity::class,
    ], version = 1, exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hackDao(): HackathonDao
    abstract fun routeDao(): RouteDao
    abstract fun skillDao(): SkillDao
    abstract fun skillTodoDao(): SkillTodoDao
    abstract fun todoDao(): TodoDao
    abstract fun userDao(): UserDao
    companion object {
        const val DATABASE_NAME = "roadmap_db"
    }
}