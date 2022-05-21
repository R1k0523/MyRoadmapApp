package ru.boringowl.myroadmapapp.data.room.repos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import ru.boringowl.myroadmapapp.data.network.HackApi
import ru.boringowl.myroadmapapp.data.network.RouteApi
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.data.room.dao.HackathonDao
import ru.boringowl.myroadmapapp.data.room.dao.RouteDao
import ru.boringowl.myroadmapapp.data.room.model.HackathonEntity
import ru.boringowl.myroadmapapp.data.room.model.RouteEntity
import ru.boringowl.myroadmapapp.model.Route
import java.util.*
import javax.inject.Inject


class RouteRepository @Inject constructor(
    private val dao: RouteDao,
    private val api: RouteApi
) {
    var isLoading by mutableStateOf(false)

    private fun entity(model: Route) = RouteEntity(model)

    suspend fun add(model: Route) = dao.insert(entity(model))
    suspend fun update(model: Route) = dao.update(entity(model))
    suspend fun delete(model: Route) = dao.delete(entity(model))
    suspend fun delete() = dao.delete()

    fun get(): Flow<List<Route>> = dao.get()
        .flowOn(Dispatchers.IO).conflate()
        .map { f -> f.map { it.toModel() } }

    suspend fun fetchAndSave() {
        withContext(Dispatchers.IO) {
            isLoading = true
            try {
                val models = api.get().items
                models.forEach { add(it) }
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}