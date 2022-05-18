package ru.boringowl.myroadmapapp.presentation.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.boringowl.myroadmapapp.presentation.repository.room.model.UserEntity
import java.util.*

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("DELETE FROM users_info WHERE user_id = :id")
    suspend fun delete(id: UUID)

    @Query("SELECT * FROM users_info")
    fun get(): Flow<List<UserEntity>>

    @Query("DELETE FROM users_info")
    suspend fun delete()

    @Query("SELECT * FROM users_info WHERE user_id = :id")
    suspend fun get(id: UUID): UserEntity?
}