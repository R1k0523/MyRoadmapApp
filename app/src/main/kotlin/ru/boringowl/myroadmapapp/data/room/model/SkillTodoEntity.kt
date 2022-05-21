package ru.boringowl.myroadmapapp.data.room.model


import androidx.room.*
import ru.boringowl.myroadmapapp.model.Route
import ru.boringowl.myroadmapapp.model.Skill
import ru.boringowl.myroadmapapp.model.SkillTodo
import ru.boringowl.myroadmapapp.model.Todo
import java.util.*

@Entity(tableName = "skilltodos")
class SkillTodoEntity (
    @PrimaryKey
    @ColumnInfo(name="skill_todo_id")
    var skillTodoId: UUID,
    @ColumnInfo(name="skill")
    var skillId: UUID,
    @ColumnInfo(name="todo")
    var todoId: UUID,
    @ColumnInfo(name="progress")
    var progress: Int = 0,
    @ColumnInfo(name="notes")
    var notes: String = "",
) {
    @ColumnInfo(name="uploaded")
    var uploaded: Boolean = true

    fun toModel(skill: Skill? = null, todo: Todo? = null): SkillTodo = SkillTodo().also {
        it.skillTodoId = skillTodoId
        it.skill = skill
        it.todo = todo
        it.progress = progress
        it.notes = notes
    }
    fun toModel(): SkillTodo = SkillTodo().also { st ->
        st.skillTodoId = skillTodoId
        st.skill = Skill().also { it.skillId = skillId }
        st.todo = Todo().also { it.todoId = todoId }
        st.progress = progress
        st.notes = notes
    }

    constructor(model: SkillTodo) : this(
        model.skillTodoId!!,
        model.skill!!.skillId!!,
        model.todo!!.todoId!!,
        model.progress,
        model.notes,
    )
}


data class SkillTodoWithSkill(
    @Embedded val skillTodo: SkillTodoEntity,
    @Relation(
        parentColumn = "skill",
        entityColumn = "skill_id"
    )
    val skill: SkillEntity
)