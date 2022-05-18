package ru.boringowl.myroadmapapp.presentation.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.boringowl.myroadmapapp.presentation.repository.room.model.SkillEntity
import java.util.*

@Dao
interface SkillDao : BaseDao<SkillEntity> {

    @Query("DELETE FROM skills WHERE skill_id = :id")
    suspend fun delete(id: UUID)

    @Query("SELECT * FROM skills")
    fun get(): Flow<List<SkillEntity>>

    @Query("DELETE FROM skills")
    suspend fun delete()

    @Query("SELECT * FROM skills WHERE skill_id = :id")
    suspend fun get(id: UUID): SkillEntity?
}