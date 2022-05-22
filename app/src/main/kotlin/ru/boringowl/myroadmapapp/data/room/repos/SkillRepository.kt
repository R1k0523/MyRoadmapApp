package ru.boringowl.myroadmapapp.data.room.repos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.boringowl.myroadmapapp.data.network.SkillApi
import ru.boringowl.myroadmapapp.data.room.dao.SkillDao
import ru.boringowl.myroadmapapp.data.room.model.SkillEntity
import ru.boringowl.myroadmapapp.model.Skill
import java.util.*
import javax.inject.Inject


class SkillRepository @Inject constructor(
    private val dao: SkillDao,
    private val api: SkillApi
) {
    var isLoading by mutableStateOf(false)

    private fun entity(model: Skill) = SkillEntity(model)

    suspend fun add(model: Skill) = dao.insert(entity(model))
    suspend fun update(model: Skill) = dao.update(entity(model))
    suspend fun delete(model: Skill) = dao.delete(entity(model))
    suspend fun delete() = dao.delete()

    fun get(): Flow<List<Skill>> = dao.get()
        .flowOn(Dispatchers.IO).conflate()
        .map { f -> f.map { it.toModel() } }

    suspend fun fetchAndSave(
        routeId: UUID,
        page: Int = 1,
        perPage: Int = 20
    ) {
        withContext(Dispatchers.IO) {
            isLoading = true
            try {
                val models = api.getByRoute(routeId, page, perPage).items
                models.forEach { add(it) }
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}