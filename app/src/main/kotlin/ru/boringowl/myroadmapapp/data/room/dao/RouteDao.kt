package ru.boringowl.myroadmapapp.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.boringowl.myroadmapapp.data.room.model.RouteEntity
import ru.boringowl.myroadmapapp.data.room.model.RouteWithSkills

@Dao
interface RouteDao : BaseDao<RouteEntity> {

    @Query("DELETE FROM routes WHERE route_id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM routes")
    fun get(): Flow<List<RouteEntity>>

    @Query("DELETE FROM routes")
    suspend fun delete()

    @Query("SELECT EXISTS(SELECT * FROM routes WHERE route_id = :id)")
    fun isExist(id : Int) : Boolean

    @Query("SELECT * FROM routes WHERE route_id = :id")
    suspend fun get(id: Int): RouteEntity?

    @Query("SELECT * FROM routes WHERE route_id = :id")
    suspend fun getWithSkills(id: Int): RouteWithSkills?
    
}