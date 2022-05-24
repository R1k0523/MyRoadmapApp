package ru.boringowl.myroadmapapp.data.room.repos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.boringowl.myroadmapapp.data.network.TodoApi
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
    private var isLoading by mutableStateOf(false)

    private val dispUploader = DispUploader({ isLoading = true }, { isLoading = false })

    private fun entity(model: Todo) = TodoEntity(model)

    suspend fun add(routeId: Int, name: String) = dispUploader.load {
        val model = api.add(routeId, name)
        model.skills?.forEach {
            todoSkillRepository.add(it)
        }
        dao.insert(entity(model))
    }

    suspend fun update(modelToUpdate: Todo) = dispUploader.load(
        onError = { dao.insert(entity(modelToUpdate).also { it.uploaded = false }) }
    ) {
        val model = api.update(modelToUpdate)
        dao.insert(entity(model))
    }

    suspend fun delete(model: Todo) = dispUploader.load {
        api.delete(model.todoId!!)
        dao.delete(entity(model))
    }

    suspend fun delete() = dao.delete()

    fun get(): Flow<List<Todo>> = dao.get()
        .flowOn(Dispatchers.IO).conflate()
        .map { f -> f.map { it.todo.toModel(it.todoSkills) } }

    suspend fun synchronizeData() = dispUploader.load {
//        dao.getNotUploaded().forEach {
//            api.update(Todo().apply {
//                this.todoId = it!!.todoId
//                this.header = it.header
//            })
//        }
        val models = api.get().items
        models.forEach {
            dao.insert(entity(it))
            it.skills?.forEach { st ->
                st.apply { todo = it }
                todoSkillRepository.add(st)
            }
        }
    }
}