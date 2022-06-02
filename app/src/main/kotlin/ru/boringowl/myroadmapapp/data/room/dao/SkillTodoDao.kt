package ru.boringowl.myroadmapapp.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.boringowl.myroadmapapp.data.room.model.SkillTodoEntity
import java.util.*

@Dao
interface SkillTodoDao : BaseDao<SkillTodoEntity> {

    @Query("DELETE FROM skilltodos WHERE skill_todo_id = :id")
    suspend fun delete(id: UUID)

    @Query("DELETE FROM skilltodos")
    suspend fun delete()

    @Query("SELECT * FROM skilltodos WHERE skill_todo_id = :id")
    suspend fun get(id: UUID): SkillTodoEntity?

    @Query("SELECT EXISTS(SELECT * FROM skilltodos WHERE skill_todo_id = :id)")
    fun isExist(id : UUID) : Boolean

    @Query("SELECT * FROM skilltodos WHERE todo = :todo")
    fun getByTodo(todo: UUID): Flow<List<SkillTodoEntity>>

    @Query("SELECT * FROM skilltodos WHERE uploaded = 0")
    suspend fun getNotUploaded(): List<SkillTodoEntity>
}