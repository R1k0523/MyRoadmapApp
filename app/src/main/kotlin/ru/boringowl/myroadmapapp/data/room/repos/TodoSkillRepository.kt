package ru.boringowl.myroadmapapp.data.room.repos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.boringowl.myroadmapapp.data.network.SkillTodoApi
import ru.boringowl.myroadmapapp.data.room.dao.SkillTodoDao
import ru.boringowl.myroadmapapp.data.room.model.SkillTodoEntity
import ru.boringowl.myroadmapapp.model.SkillTodo
import java.util.*
import javax.inject.Inject


class TodoSkillRepository @Inject constructor(
    private val dao: SkillTodoDao,
    private val api: SkillTodoApi
) {
    private var isLoading by mutableStateOf(false)

    private val dispUploader = DispUploader({ isLoading = true }, { isLoading = false })

    private fun entity(model: SkillTodo) = SkillTodoEntity(model)

    suspend fun add(model: SkillTodo, upload: Boolean = false) = dispUploader.load {
        if (upload) {
            val uploaded = api.add(model)
            dao.insert(entity(uploaded))
        } else {
            dao.insert(entity(model))
        }
    }

    suspend fun update(model: SkillTodo) = dispUploader.load(
        onError = { dao.insert(entity(model).also { it.uploaded = false }) }
    ) {
        val uploaded = api.update(model)
        dao.insert(entity(uploaded))
    }

    suspend fun delete(model: SkillTodo) = dispUploader.load {
        api.delete(model.skillTodoId!!)
        dao.delete(entity(model))
    }

    suspend fun delete() = dao.delete()

    fun get(todoId: UUID): Flow<List<SkillTodo>> =
        dao.getByTodo(todoId)
            .flowOn(Dispatchers.IO).conflate()
            .map { f ->
                f.map {
                    it.skillTodo.toModel(skill = it.skill.toModel())
                }
            }

    suspend fun synchronizeData(todoId: UUID) = dispUploader.load {
        dao.getNotUploaded().forEach { api.update(it.toModel()) }
        val models = api.get(todoId).items
        models.forEach { dao.insert(entity(it)) }
    }
}