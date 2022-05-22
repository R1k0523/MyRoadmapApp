//package ru.boringowl.myroadmapapp.data.room.repos
//
//import androidx.paging.*
//import retrofit2.HttpException
//import ru.boringowl.myroadmapapp.data.network.HackApi
//import ru.boringowl.myroadmapapp.data.room.dao.HackathonDao
//import ru.boringowl.myroadmapapp.data.room.model.HackathonEntity
//import ru.boringowl.myroadmapapp.model.Hackathon
//import ru.boringowl.myroadmapapp.model.User
//import java.io.IOException
//
//@OptIn(ExperimentalPagingApi::class)
//class ExampleRemoteMediator(
//  private val dao: HackathonDao,
//  private val api: HackApi
//) : RemoteMediator<Int, Hackathon>() {
//
//
//  override suspend fun load(
//    loadType: LoadType,
//    state: PagingState<Int, Hackathon>
//  ): MediatorResult {
//    return try {
//      val loadKey = when (loadType) {
//        LoadType.REFRESH -> null
//        LoadType.PREPEND -> return MediatorResult.Success(
//          endOfPaginationReached = true
//        )
//        LoadType.APPEND -> {
//          val lastItem = state.lastItemOrNull()
//              ?: return MediatorResult.Success(
//                endOfPaginationReached = true
//              )
//          lastItem.hackId
//        }
//      }
//      val items = api.get().items.map { HackathonEntity(it) }
//
//      if (loadType == LoadType.REFRESH) {
//        dao.clearAndInsert(items)
//      }
//      dao.insert(items)
//      MediatorResult.Success(
//        endOfPaginationReached = items.isEmpty()
//      )
//    } catch (e: IOException) {
//      MediatorResult.Error(e)
//    } catch (e: HttpException) {
//      MediatorResult.Error(e)
//    }
//  }
//}
//
//class MovieSource(
//  private val repository: HackathonRepository
//) : PagingSource<Int, Hackathon>() {
//
//  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hackathon> {
//    return try {
//      val nextPage = params.key ?: 1
//      val movieListResponse = repository.fetchAndSave(nextPage)
//
//      LoadResult.Page(
//        data = movieListResponse.results,
//        prevKey = if (nextPage == 1) null else nextPage - 1,
//        nextKey = movieListResponse.page.plus(1)
//      )
//    } catch (e: Exception) {
//      LoadResult.Error(e)
//    }
//  }
//}