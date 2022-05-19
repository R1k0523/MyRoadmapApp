package ru.boringowl.myroadmapapp.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.boringowl.myroadmapapp.model.UserRole
import java.util.*


@Entity(tableName = "users_info")
class UserEntity(
    @PrimaryKey
    @ColumnInfo(name="user_id")
    var userId: UUID,
    @ColumnInfo(name="username")
    var username: String = "",
    @ColumnInfo(name="email")
    var email: String = "",
    @ColumnInfo(name="role")
    var role: UserRole,
)
