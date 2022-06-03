package ru.boringowl.myroadmapapp.data.room.repos

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.boringowl.myroadmapapp.data.network.api.BookPostApi
import ru.boringowl.myroadmapapp.data.network.mediators.BookRemoteMediator
import ru.boringowl.myroadmapapp.data.room.AppDatabase
import ru.boringowl.myroadmapapp.data.room.dao.BookPostDao
import ru.boringowl.myroadmapapp.data.room.model.BookPostEntity
import ru.boringowl.myroadmapapp.model.BookPost
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class BooksRepository @Inject constructor(
    private val dao: BookPostDao,
    private val api: BookPostApi,
    private val db: AppDatabase,
) {

    private fun entity(model: BookPost) = BookPostEntity(model)

    suspend fun add(model: BookPost) = dao.insert(entity(model))
    suspend fun update(model: BookPost) = dao.update(entity(model))
    suspend fun delete(model: BookPost) = dao.delete(entity(model))

    suspend fun delete() = dao.delete()

    fun get(routeId: Int, query: String): Flow<PagingData<BookPost>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = BookRemoteMediator(
                api = api,
                db = db,
                query = query,
                routeId = routeId
            ),
            pagingSourceFactory = { dao.getByRoute(routeId) }
        ).flow.map {f -> f.map { it.toModel() } }
    }
}