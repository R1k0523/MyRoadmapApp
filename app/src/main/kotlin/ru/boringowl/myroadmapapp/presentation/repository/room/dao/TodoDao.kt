package ru.boringowl.myroadmapapp.presentation.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.boringowl.myroadmapapp.presentation.repository.room.model.TodoEntity
import ru.boringowl.myroadmapapp.presentation.repository.room.model.TodoWithSkills
import java.util.*

@Dao
interface TodoDao : BaseDao<TodoEntity> {

    @Query("DELETE FROM todos WHERE todo_id = :id")
    suspend fun delete(id: UUID)

    @Query("SELECT * FROM todos")
    fun get(): Flow<List<TodoEntity>>

    @Query("DELETE FROM todos")
    suspend fun delete()

    @Query("SELECT * FROM todos WHERE todo_id = :id")
    suspend fun get(id: UUID): TodoWithSkills?
    
}