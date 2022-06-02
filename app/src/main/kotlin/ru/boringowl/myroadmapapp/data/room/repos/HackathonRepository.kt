package ru.boringowl.myroadmapapp.data.room.repos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import ru.boringowl.myroadmapapp.data.network.HackApi
import ru.boringowl.myroadmapapp.data.network.HackRemoteMediator
import ru.boringowl.myroadmapapp.data.room.AppDatabase
import ru.boringowl.myroadmapapp.data.room.dao.HackathonDao
import ru.boringowl.myroadmapapp.data.room.model.HackathonEntity
import ru.boringowl.myroadmapapp.model.Hackathon
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class HackathonRepository @Inject constructor(
    private val dao: HackathonDao,
    private val api: HackApi,
    private val db: AppDatabase,
) {
    var isLoading by mutableStateOf(false)

    private fun entity(model: Hackathon) = HackathonEntity(model)

    suspend fun add(model: Hackathon) = dao.insert(entity(model))
    suspend fun update(model: Hackathon) = dao.update(entity(model))
    suspend fun delete(model: Hackathon) = dao.delete(entity(model))
    suspend fun addIfNotExists(model: Hackathon) {
        model.hackId?.let { id ->
            if (!dao.isExist(id))
                add(model)
        }
    }
    suspend fun delete() = dao.delete()

    fun get(query: String): Flow<PagingData<Hackathon>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = HackRemoteMediator(
                api = api,
                db = db,
                query = query
            ),
            pagingSourceFactory = { dao.get() }
        ).flow.map {f -> f.map { it.toModel() } }
    }
}