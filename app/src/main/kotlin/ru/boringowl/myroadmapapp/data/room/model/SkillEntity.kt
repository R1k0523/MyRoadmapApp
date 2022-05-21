package ru.boringowl.myroadmapapp.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.boringowl.myroadmapapp.model.Route
import ru.boringowl.myroadmapapp.model.Skill
import java.util.*

@Entity(tableName = "skills")
class SkillEntity (
    @PrimaryKey
    @ColumnInfo(name="skill_id")
    var skillId: UUID,
    @ColumnInfo(name="skill_name")
    var skillName: String = "",
    @ColumnInfo(name="necessity")
    var necessity: Int = 0,
    @ColumnInfo(name="route")
    var route_id: Int? = null
) {
    fun toModel(route: Route? = null): Skill = Skill().also {
        it.skillId = skillId
        it.skillName = skillName
        it.necessity = necessity
        it.route = route
    }

    constructor(model: Skill) : this(
        model.skillId!!,
        model.skillName,
        model.necessity,
        model.route?.routeId,
    )
}
