package ru.boringowl.myroadmapapp.presentation.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.boringowl.myroadmapapp.presentation.repository.model.*


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
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "roadmap_db"
    }
}