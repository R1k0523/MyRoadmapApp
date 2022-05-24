package ru.boringowl.myroadmapapp.data.room.model


import androidx.room.*
import ru.boringowl.myroadmapapp.model.Todo
import java.util.*

@Entity(tableName = "todos")
class TodoEntity (
    @PrimaryKey
    @ColumnInfo(name="todo_id")
    var todoId: UUID,
    @ColumnInfo(name="header")
    var header: String = "",
)  {
    @ColumnInfo(name="uploaded")
    var uploaded: Boolean = true
    fun toModel(todoSkills: List<SkillTodoEntity>?): Todo = Todo().also {
        it.todoId = todoId
        it.header = header
        it.skills = todoSkills?.map { ts -> ts.toModel() }
    }

    constructor(model: Todo) : this(
        model.todoId!!,
        model.header
    )
}

data class TodoWithSkills(
    @Embedded val todo: TodoEntity,
    @Relation(
        parentColumn = "todo_id",
        entityColumn = "todo"
    )
    val todoSkills: List<SkillTodoEntity>
)