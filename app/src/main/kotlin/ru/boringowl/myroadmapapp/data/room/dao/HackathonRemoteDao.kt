package ru.boringowl.myroadmapapp.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.boringowl.myroadmapapp.data.room.model.HackathonEntity
import ru.boringowl.myroadmapapp.data.room.model.HackathonRemoteKeys
import java.util.*


@Dao
interface HackathonRemoteDao : BaseDao<HackathonRemoteKeys> {

    @Query("DELETE FROM hackathon_keys")
    suspend fun delete()

    @Query("SELECT * FROM hackathon_keys WHERE id = :id")
    suspend fun get(id: UUID): HackathonRemoteKeys?
}