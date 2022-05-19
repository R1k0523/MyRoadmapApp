package ru.boringowl.myroadmapapp.data.room.model

import androidx.room.*


@Entity(tableName = "routes")
class RouteEntity(
    @PrimaryKey
    @ColumnInfo(name="route_id")
    var routeId: Int? = null,
    @ColumnInfo(name="route_name")
    var routeName: String = "",
    @ColumnInfo(name="route_description")
    var routeDescription: String = "",
    @ColumnInfo(name="resumes_count")
    var resumesCount: Int = 0,
    @ColumnInfo(name="vacancies_count")
    var vacanciesCount: Int = 0,
)

data class RouteWithSkills(
    @Embedded val route: RouteEntity,
    @Relation(
        parentColumn = "route_id",
        entityColumn = "skill_id"
    )
    val cars: List<SkillEntity>
)