package ru.boringowl.myroadmapapp.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.boringowl.myroadmapapp.model.Hackathon
import java.util.*

@Entity(tableName = "hackathons")
class HackathonEntity(
    @PrimaryKey
    @ColumnInfo(name="hack_id")
    var hackId: UUID,
    @ColumnInfo(name="hack_title")
    var hackTitle: String = "",
    @ColumnInfo(name="hack_description")
    var hackDescription: String = "",
    @ColumnInfo(name="publish_date")
    var publishDate: String,
    @ColumnInfo(name="source")
    var source: String = "",
    @ColumnInfo(name="date")
    var date: String? = "",
    @ColumnInfo(name="registration")
    var registration: String? = "",
    @ColumnInfo(name="focus")
    var focus: String? = "",
    @ColumnInfo(name="prize")
    var prize: String? = "",
    @ColumnInfo(name="routes")
    var routes: String? = "",
    @ColumnInfo(name="terms")
    var terms: String? = "",
    @ColumnInfo(name="organization")
    var organization: String? = "",
    @ColumnInfo(name="image_url")
    var imageUrl: String? = "",
) {

    fun toModel(): Hackathon = Hackathon().also {
        it.hackId = hackId
        it.hackTitle = hackTitle
        it.hackDescription = hackDescription
        it.publishDate = publishDate
        it.source = source
        it.date = date
        it.registration = registration
        it.focus = focus
        it.prize = prize
        it.routes = routes
        it.terms = terms
        it.organization = organization
        it.imageUrl = imageUrl
    }

    constructor(model: Hackathon) : this(
        model.hackId!!,
        model.hackTitle,
        model.hackDescription,
        model.publishDate,
        model.source,
        model.date,
        model.registration,
        model.focus,
        model.prize,
        model.routes,
        model.terms,
        model.organization,
        model.imageUrl,
    )
}
