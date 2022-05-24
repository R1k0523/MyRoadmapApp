package ru.boringowl.myroadmapapp.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.boringowl.myroadmapapp.data.room.model.HackathonEntity
import java.util.*


@Dao
interface HackathonDao : BaseDao<HackathonEntity> {

    @Query("DELETE FROM hackathons WHERE hack_id = :id")
    suspend fun delete(id: UUID)

    @Query("SELECT * FROM hackathons")
    fun get(): Flow<List<HackathonEntity>>

    @Query("DELETE FROM hackathons")
    suspend fun delete()

    @Query("SELECT * FROM hackathons WHERE hack_id = :id")
    suspend fun get(id: UUID): HackathonEntity?

    @Transaction
    suspend fun clearAndInsert(items: List<HackathonEntity>) {
        delete()
        insert(items)
    }
}