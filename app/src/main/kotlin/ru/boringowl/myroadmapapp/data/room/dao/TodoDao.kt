package ru.boringowl.myroadmapapp.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.boringowl.myroadmapapp.data.room.model.TodoEntity
import ru.boringowl.myroadmapapp.data.room.model.TodoWithSkills
import java.util.*

@Dao
interface TodoDao : BaseDao<TodoEntity> {

    @Query("DELETE FROM todos WHERE todo_id = :id")
    suspend fun delete(id: UUID)

    @Query("SELECT * FROM todos")
    fun get(): Flow<List<TodoWithSkills>>

    @Query("SELECT * FROM todos")
    fun getList(): List<TodoEntity>

    @Query("DELETE FROM todos")
    suspend fun delete()

    @Query("SELECT EXISTS(SELECT * FROM todos WHERE todo_id = :id)")
    fun isExist(id : UUID) : Boolean

    @Query("SELECT * FROM todos WHERE todo_id = :id")
    fun get(id: UUID): Flow<TodoWithSkills?>

    @Query("SELECT * FROM todos WHERE todo_id = :id")
    fun getItem(id: UUID): TodoEntity

    @Query("SELECT * FROM todos WHERE uploaded = 0")
    suspend fun getNotUploaded(): List<TodoEntity?>

    @Query("DELETE FROM todos WHERE todo_id = :id")
    suspend fun deleteById(id: UUID)
}