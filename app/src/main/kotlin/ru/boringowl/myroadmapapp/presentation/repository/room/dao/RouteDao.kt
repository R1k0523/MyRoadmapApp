package ru.boringowl.myroadmapapp.presentation.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.boringowl.myroadmapapp.presentation.repository.room.model.RouteEntity
import ru.boringowl.myroadmapapp.presentation.repository.room.model.RouteWithSkills
import java.util.*

@Dao
interface RouteDao : BaseDao<RouteEntity> {

    @Query("DELETE FROM routes WHERE route_id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM routes")
    fun get(): Flow<List<RouteEntity>>

    @Query("DELETE FROM routes")
    suspend fun delete()

    @Query("SELECT * FROM routes WHERE route_id = :id")
    suspend fun get(id: UUID): RouteWithSkills?
    
}