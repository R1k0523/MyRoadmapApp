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
import ru.boringowl.myroadmapapp.data.network.RouteApi
import ru.boringowl.myroadmapapp.data.room.dao.RouteDao
import ru.boringowl.myroadmapapp.data.room.model.RouteEntity
import ru.boringowl.myroadmapapp.model.Route
import javax.inject.Inject


class RouteRepository @Inject constructor(
    private val dao: RouteDao,
    private val api: RouteApi
) {

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
            try {
                val models = api.get().items
                models.forEach { add(it) }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}