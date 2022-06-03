package ru.boringowl.myroadmapapp.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import ru.boringowl.myroadmapapp.data.room.model.BookPostEntity
import java.util.*

@Dao
interface BookPostDao : BaseDao<BookPostEntity> {

    @Query("DELETE FROM book_posts WHERE book_post_id = :id")
    suspend fun delete(id: UUID)

    @Query("SELECT * FROM book_posts WHERE route_id = :id")
    fun getByRoute(id: Int): PagingSource<Int, BookPostEntity>

    @Query("DELETE FROM book_posts")
    suspend fun delete()

    @Query("SELECT EXISTS(SELECT * FROM book_posts WHERE book_post_id = :id)")
    fun isExist(id : UUID) : Boolean

    @Query("SELECT * FROM book_posts WHERE book_post_id = :id")
    suspend fun get(id: UUID): BookPostEntity?
}