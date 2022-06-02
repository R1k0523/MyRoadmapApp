package ru.boringowl.myroadmapapp.model


import java.util.*

class Todo() {
    var todoId: UUID? = null
    var header = ""
    var user: User? = null
    var skills: List<SkillTodo>? = listOf()
    var ready: Int = 0
    var full: Int = 0

    constructor(todoId: UUID) : this() { this.todoId = todoId }
}
