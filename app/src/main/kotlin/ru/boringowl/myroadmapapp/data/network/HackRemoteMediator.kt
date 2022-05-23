package ru.boringowl.myroadmapapp.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.boringowl.myroadmapapp.data.room.AppDatabase
import ru.boringowl.myroadmapapp.data.room.model.HackathonEntity
import ru.boringowl.myroadmapapp.data.room.model.HackathonRemoteKeys
import ru.boringowl.myroadmapapp.model.Hackathon

@ExperimentalPagingApi
class HackRemoteMediator(
    private val api: HackApi,
    private val db: AppDatabase
) : RemoteMediator<Int, HackathonEntity>() {

    private val dao = db.hackDao()
    private val remoteDao = db.hackRemoteDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HackathonEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.next?.minus(1) ?: 1
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

            val response = api.get(page = currentPage, perPage = 20)

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.delete()
                    remoteDao.delete()
                }
                val keys = response.items.map { i ->
                    HackathonRemoteKeys(
                        id = i.hackId!!,
                        prev = response.prevPage,
                        next = response.nextPage
                    )
                }
                remoteDao.insert(keys)
                dao.insert(response.items.map { HackathonEntity(it) })
            }
            MediatorResult.Success(endOfPaginationReached = response.nextPage == null)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, HackathonEntity>
    ): HackathonRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.hackId?.let { id ->
                remoteDao.get(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, HackathonEntity>
    ): HackathonRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { h ->
                remoteDao.get(h.hackId!!)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, HackathonEntity>
    ): HackathonRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { h ->
                remoteDao.get(h.hackId!!)
            }
    }

}