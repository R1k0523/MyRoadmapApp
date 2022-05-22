package ru.boringowl.myroadmapapp.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.boringowl.myroadmapapp.data.room.model.UserEntity
import java.util.*

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("DELETE FROM users_info WHERE user_id = :id")
    suspend fun delete(id: UUID)

    @Query("SELECT * FROM users_info LIMIT 1")
    fun get(): Flow<UserEntity?>

    @Query("DELETE FROM users_info")
    suspend fun delete()
}