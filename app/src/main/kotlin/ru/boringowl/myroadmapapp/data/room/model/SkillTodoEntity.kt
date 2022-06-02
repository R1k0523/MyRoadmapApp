package ru.boringowl.myroadmapapp.data.room.model


import androidx.room.*
import ru.boringowl.myroadmapapp.model.Skill
import ru.boringowl.myroadmapapp.model.SkillTodo
import ru.boringowl.myroadmapapp.model.Todo
import java.util.*

@Entity(tableName = "skilltodos")
class SkillTodoEntity (
    @PrimaryKey
    @ColumnInfo(name="skill_todo_id")
    var skillTodoId: UUID,
    @ColumnInfo(name="skill_name")
    var skillName: String = "",
    @ColumnInfo(name="manual_name")
    var manualName: String = "",
    @ColumnInfo(name="todo")
    var todoId: UUID,
    @ColumnInfo(name="progress")
    var progress: Int = 0,
    @ColumnInfo(name="necessity")
    var necessity: Int = 0,
    @ColumnInfo(name="notes")
    var notes: String = "",
    @ColumnInfo(name="binary_progress")
    var binaryProgress: Boolean = false,
    @ColumnInfo(name="favorite")
    var favorite: Boolean = false,
) {
    @ColumnInfo(name="uploaded")
    var uploaded: Boolean = true

    fun toModel(todo: Todo? = null): SkillTodo = SkillTodo().also {
        it.skillTodoId = skillTodoId
        it.skillName = skillName
        it.manualName = manualName
        it.todo = todo
        it.progress = progress
        it.necessity = necessity
        it.notes = notes
        it.binaryProgress = binaryProgress
        it.favorite = favorite
    }

    constructor(model: SkillTodo, todoId: UUID? = null) : this(
        model.skillTodoId!!,
        model.skillName,
        model.manualName,
        model.todo?.todoId ?: todoId!!,
        model.progress,
        model.necessity,
        model.notes,
        model.binaryProgress,
        model.favorite,
    )
}

