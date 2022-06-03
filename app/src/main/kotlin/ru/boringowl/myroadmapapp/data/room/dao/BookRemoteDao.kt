package ru.boringowl.myroadmapapp.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import ru.boringowl.myroadmapapp.data.room.model.BookPostRemoteKeys
import java.util.*


@Dao
interface BookRemoteDao : BaseDao<BookPostRemoteKeys> {

    @Query("DELETE FROM book_keys")
    suspend fun delete()

    @Query("SELECT * FROM book_keys WHERE id = :id")
    suspend fun get(id: UUID): BookPostRemoteKeys?
}