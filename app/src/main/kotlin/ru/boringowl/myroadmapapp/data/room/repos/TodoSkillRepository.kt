package ru.boringowl.myroadmapapp.data.room.repos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.boringowl.myroadmapapp.data.network.api.SkillTodoApi
import ru.boringowl.myroadmapapp.data.room.dao.SkillTodoDao
import ru.boringowl.myroadmapapp.data.room.dao.TodoDao
import ru.boringowl.myroadmapapp.data.room.model.SkillTodoEntity
import ru.boringowl.myroadmapapp.model.SkillTodo
import ru.boringowl.myroadmapapp.model.Todo
import java.util.*
import javax.inject.Inject


class TodoSkillRepository @Inject constructor(
    private val dao: SkillTodoDao,
    private val todoDao: TodoDao,
    private val api: SkillTodoApi
) {

    private fun entity(model: SkillTodo, todoId: UUID? = null) = SkillTodoEntity(model, todoId)

    suspend fun add(model: SkillTodo, upload: Boolean = false) = loadWithIO {
        if (upload) {
            val uploaded = api.add(model)
            val entity = entity(uploaded)
            if (uploaded.todo?.todoId != null)
                entity.todoId = model.todo!!.todoId!!
            dao.insert(entity)
        } else {
            dao.insert(entity(model))
        }
    }

    suspend fun update(model: SkillTodo) = loadWithIO (
        onError = { dao.update(entity(model).also { it.uploaded = false }) }
    ) {
        val uploaded = api.update(model)
        val entity = entity(uploaded)
        if (uploaded.todo?.todoId != null)
            entity.todoId = model.todo!!.todoId!!
        dao.update(entity(uploaded))
    }

    suspend fun delete(model: SkillTodo) = loadWithIO {
        api.delete(model.skillTodoId!!)
        dao.delete(entity(model))
    }

    suspend fun delete() = dao.delete()

    fun get(todoId: UUID): Flow<Todo?> =
        todoDao.get(todoId).flowOn(Dispatchers.IO).conflate().map { f ->
            f?.let { it.todo.toModel(it.todoSkills) }
        }


    suspend fun uploadLocal() = loadWithIO {
        dao.getNotUploaded().forEach {
            api.update(it.toModel())
        }
    }

    suspend fun synchronizeData(todoId: UUID) = loadWithIO {
        val models = api.getByTodo(todoId).items
        models.forEach { dao.insert(entity(it)) }
        val todo = todoDao.getItem(todoId)
        todo.ready = models.sumOf { it.progress }
        todo.full = models.size * 5
        todoDao.update(todo)
    }
}