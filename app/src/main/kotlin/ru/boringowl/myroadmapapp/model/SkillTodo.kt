package ru.boringowl.myroadmapapp.model


import java.util.*

class SkillTodo {
    var skillTodoId: UUID? = null
    var skillName: String = ""
    var manualName: String = ""
    var todo: Todo? = null
    var progress: Int = 0
    var necessity: Int = 0
    var notes: String = ""
    var binaryProgress: Boolean = false
    var favorite: Boolean = false
    fun name() = (if (manualName == skillName) skillName else "$manualName ($skillName)")
        .replaceFirstChar { it.titlecase(Locale.getDefault()) }
    fun fullText() = listOf(notes, manualName, skillName).joinToString(" ").lowercase()
    override fun toString() = "skillName: $skillName\n" +
            "manualName: $manualName\n" +
            "progress: $progress\n" +
            "necessity: $necessity\n" +
            "notes: $notes\n" +
            "todo: ${todo?.todoId}\n" +
            "favorite: $favorite"
}
