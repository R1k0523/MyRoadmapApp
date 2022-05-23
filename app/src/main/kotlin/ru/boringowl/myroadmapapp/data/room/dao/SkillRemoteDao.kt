package ru.boringowl.myroadmapapp.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import ru.boringowl.myroadmapapp.data.room.model.SkillRemoteKeys
import java.util.*


@Dao
interface SkillRemoteDao : BaseDao<SkillRemoteKeys> {
    @Query("DELETE FROM skill_keys")
    suspend fun delete()
    @Query("SELECT * FROM skill_keys WHERE id = :id")
    suspend fun get(id: UUID): SkillRemoteKeys?
}