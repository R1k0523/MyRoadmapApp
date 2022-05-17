package ru.boringowl.myroadmapapp.model


import java.util.*

class Todo() {
    var todoId: UUID? = null
    var header = ""
    var user: User? = null
    var skills: List<SkillTodo>? = listOf()
}
