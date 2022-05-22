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
import ru.boringowl.myroadmapapp.data.network.HackApi
import ru.boringowl.myroadmapapp.data.room.dao.HackathonDao
import ru.boringowl.myroadmapapp.data.room.model.HackathonEntity
import ru.boringowl.myroadmapapp.model.Hackathon
import javax.inject.Inject


class HackathonRepository @Inject constructor(
    private val dao: HackathonDao,
    private val api: HackApi
) {
    var isLoading by mutableStateOf(false)

    private fun entity(model: Hackathon) = HackathonEntity(model)

    suspend fun add(model: Hackathon) = dao.insert(entity(model))
    suspend fun update(model: Hackathon) = dao.update(entity(model))
    suspend fun delete(model: Hackathon) = dao.delete(entity(model))
    suspend fun delete() = dao.delete()

    fun get(): Flow<List<Hackathon>> = dao.get()
        .flowOn(Dispatchers.IO).conflate()
        .map { f -> f.map { it.toModel() } }

    suspend fun fetchAndSave(page: Int = 1,
                             perPage: Int = 20) {
        withContext(Dispatchers.IO) {
            isLoading = true
            try {
                val models = api.get(page, perPage).items
                models.forEach { add(it) }
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}