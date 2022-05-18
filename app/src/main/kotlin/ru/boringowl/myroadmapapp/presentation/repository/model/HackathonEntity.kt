package ru.boringowl.myroadmapapp.presentation.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "hackathons")
class HackathonEntity {
    @PrimaryKey
    @ColumnInfo(name="hack_id")
    var hackId: UUID? = null
    @ColumnInfo(name="hack_title")
    var hackTitle: String = ""
    @ColumnInfo(name="hack_description")
    var hackDescription: String = ""
    @ColumnInfo(name="publish_date")
    var publishDate: LocalDateTime = LocalDateTime.now()
    @ColumnInfo(name="source")
    var source: String = ""
    @ColumnInfo(name="date")
    var date: String? = ""
    @ColumnInfo(name="registration")
    var registration: String? = ""
    @ColumnInfo(name="focus")
    var focus: String? = ""
    @ColumnInfo(name="prize")
    var prize: String? = ""
    @ColumnInfo(name="routes")
    var routes: String? = ""
    @ColumnInfo(name="terms")
    var terms: String? = ""
    @ColumnInfo(name="organization")
    var organization: String? = ""
    @ColumnInfo(name="image_url")
    var imageUrl: String? = ""
}
