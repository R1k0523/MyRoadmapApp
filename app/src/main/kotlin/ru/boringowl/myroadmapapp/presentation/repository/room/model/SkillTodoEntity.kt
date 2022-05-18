package ru.boringowl.myroadmapapp.presentation.repository.room.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "skilltodos")
class SkillTodoEntity (
    @PrimaryKey
    @ColumnInfo(name="skill_todo_id")
    var skillTodoId: UUID? = null,
    @ColumnInfo(name="skill")
    var skillId: UUID? = null,
    @ColumnInfo(name="todo")
    var todoId: UUID? = null,
    @ColumnInfo(name="progress")
    var progress: Int = 0,
    @ColumnInfo(name="notes")
    var notes: String = "",
)
