package ru.boringowl.myroadmapapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.boringowl.myroadmapapp.data.room.converters.RoomConverters
import ru.boringowl.myroadmapapp.data.room.dao.*
import ru.boringowl.myroadmapapp.data.room.model.*


@Database(
    entities = [
        HackathonEntity::class,
        RouteEntity::class,
        SkillEntity::class,
        SkillTodoEntity::class,
        TodoEntity::class,
        UserEntity::class,
        SkillRemoteKeys::class,
        HackathonRemoteKeys::class,
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
    abstract fun skillRemoteDao(): SkillRemoteDao
    abstract fun hackRemoteDao(): HackathonRemoteDao
    companion object {
        const val DATABASE_NAME = "roadmap_db"
    }
}