package ru.boringowl.myroadmapapp.presentation.repository.room.model


import androidx.room.*
import java.util.*

@Entity(tableName = "todos")
class TodoEntity (
    @PrimaryKey
    @ColumnInfo(name="todo_id")
    var todoId: UUID? = null,
    @ColumnInfo(name="header")
    var header: String = "",
)

data class TodoWithSkills(
    @Embedded val todo: TodoEntity,
    @Relation(
        parentColumn = "todo_id",
        entityColumn = "todo"
    )
    val todoSkills: List<SkillTodoEntity>
)