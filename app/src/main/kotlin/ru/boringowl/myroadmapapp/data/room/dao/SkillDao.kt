package ru.boringowl.myroadmapapp.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.boringowl.myroadmapapp.data.room.model.SkillEntity
import ru.boringowl.myroadmapapp.data.room.model.SkillWithRoute
import java.util.*

@Dao
interface SkillDao : BaseDao<SkillEntity> {

    @Query("DELETE FROM skills WHERE skill_id = :id")
    suspend fun delete(id: UUID)

    @Query("SELECT * FROM skills WHERE route = :id")
    fun getByRoute(id: Int): Flow<List<SkillWithRoute>>

    @Query("DELETE FROM skills")
    suspend fun delete()

    @Query("SELECT EXISTS(SELECT * FROM skills WHERE skill_id = :id)")
    fun isExist(id : Int) : Boolean

    @Query("SELECT * FROM skills WHERE skill_id = :id")
    suspend fun get(id: UUID): SkillEntity?
}