package ru.boringowl.myroadmapapp.data.room.repos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import ru.boringowl.myroadmapapp.data.network.HackApi
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.data.room.dao.HackathonDao
import ru.boringowl.myroadmapapp.data.room.model.HackathonEntity
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

    fun get(): Flow<List<HackathonEntity>> = dao.get().flowOn(Dispatchers.IO).conflate()

    suspend fun fetchAndSave(page: Int = 1,
                             perPage: Int = 20) {
        withContext(Dispatchers.IO) {
            isLoading = true
            try {
                val hackathons = api.get(page, perPage).items
                hackathons.forEach { add(it) }
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}