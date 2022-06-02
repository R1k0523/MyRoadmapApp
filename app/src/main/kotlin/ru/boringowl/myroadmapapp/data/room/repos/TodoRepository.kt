package ru.boringowl.myroadmapapp.data.room.repos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import ru.boringowl.myroadmapapp.data.network.TodoApi
import ru.boringowl.myroadmapapp.data.network.errorText
import ru.boringowl.myroadmapapp.data.room.dao.SkillDao
import ru.boringowl.myroadmapapp.data.room.dao.SkillTodoDao
import ru.boringowl.myroadmapapp.data.room.dao.TodoDao
import ru.boringowl.myroadmapapp.data.room.model.TodoEntity
import ru.boringowl.myroadmapapp.model.Todo
import java.util.*
import javax.inject.Inject


class TodoRepository @Inject constructor(
    private val dao: TodoDao,
    private val todoSkillRepository: TodoSkillRepository,
    private val api: TodoApi
) {

    private fun entity(model: Todo) = TodoEntity(model)

    suspend fun add(
        routeId: Int,
        name: String,
        onError: suspend () -> Unit = {},
        onSuccess: suspend () -> Unit = {}
    ) = loadWithIO(
        onError = onError,
        onSuccess = onSuccess
    ) {
        val model = api.add(routeId, name)
        model.skills?.forEach {
            it.todo = model
            todoSkillRepository.add(it)
        }
        dao.insert(entity(model))
    }

    suspend fun update(modelToUpdate: Todo) = loadWithIO(
        onError = { dao.insert(entity(modelToUpdate).also { it.uploaded = false }) }
    ) {
        val model = api.update(modelToUpdate)
        dao.insert(entity(model))
    }

    suspend fun delete(model: Todo) = loadWithIO {
        api.delete(model.todoId!!)
        dao.deleteById(model.todoId!!)
    }

    suspend fun delete() = loadWithIO {
        val todos = dao.get()
        dao.delete()
        todos.collect{ ls ->
            ls.forEach {
                api.delete(it.todo.todoId)
            }
        }
    }

    fun get(): Flow<List<Todo>> = dao.get()
        .flowOn(Dispatchers.IO).conflate()
        .map { f -> f.map { it.todo.toModel(it.todoSkills) } }

    suspend fun synchronizeData() = loadWithIO {
        todoSkillRepository.uploadLocal()
        val models = api.get().items
        models.forEach {
            dao.insert(entity(it))
        }
        val ids = models.map { it.todoId }
        dao.getList().filter { it.todoId !in ids }.forEach { t->
            t.todoId.let {dao.deleteById(it)}
        }
    }
}