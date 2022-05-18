package ru.boringowl.myroadmapapp.presentation.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "skills")
class SkillEntity (
    @PrimaryKey
    @ColumnInfo(name="skill_id")
    var skillId: UUID? = null,
    @ColumnInfo(name="skill_name")
    var skillName: String = "",
    @ColumnInfo(name="necessity")
    var necessity: Int = 0,
    @ColumnInfo(name="route")
    var route_id: Int? = null
)
