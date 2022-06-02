package ru.boringowl.myroadmapapp.data.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.boringowl.myroadmapapp.data.room.AppDatabase
import ru.boringowl.myroadmapapp.data.room.model.BookPostEntity
import ru.boringowl.myroadmapapp.data.room.model.BookPostRemoteKeys

@ExperimentalPagingApi
class BookRemoteMediator(
    private val api: BookPostApi,
    private val db: AppDatabase,
    private val query: String,
    private val routeId: Int
) : RemoteMediator<Int, BookPostEntity>() {

    private val dao = db.bookPostDao()
    private val remoteDao = db.bookRemoteDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BookPostEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.next?.minus(1) ?: 0
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prev
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.next
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = api.get(id = routeId, page = currentPage, limit = 10, query = query)
            val prev = if (currentPage in (1 until response.totalPages)) currentPage - 1 else null
            val next = if (currentPage in (0 until response.totalPages - 1)) currentPage + 1 else null
            Log.e("BOOKS", "CURR: $currentPage PREV: $prev NEXT: $next")
            db.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        dao.delete()
                        remoteDao.delete()
                    }
                    val keys = response.content.map { i ->
                        BookPostRemoteKeys(
                            id = i.bookPostId!!,
                            prev = prev,
                            next = next
                        )
                    }
                    remoteDao.insert(keys)
                    dao.insert(response.content.map { b -> BookPostEntity(b).also { it.routeId = routeId } })
            }
            MediatorResult.Success(endOfPaginationReached = next == null)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, BookPostEntity>
    ): BookPostRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.bookPostId?.let { id ->
                remoteDao.get(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, BookPostEntity>
    ): BookPostRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { h ->
                remoteDao.get(h.bookPostId)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, BookPostEntity>
    ): BookPostRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { h ->
                remoteDao.get(h.bookPostId)
            }
    }

}