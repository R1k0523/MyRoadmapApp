package ru.boringowl.myroadmapapp.model


import java.util.*

class SkillTodo {
    var skillTodoId: UUID? = null
    var skill: Skill? = null
    var todo: Todo? = null
    var progress: Int = 0
    var notes: String = ""
}
