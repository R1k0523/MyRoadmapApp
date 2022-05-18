package ru.boringowl.myroadmapapp.presentation.repository.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.boringowl.myroadmapapp.model.Hackathon
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "hackathons")
class HackathonEntity() {
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

    constructor(model: Hackathon) : this() {
        this.hackId = model.hackId
        this.hackTitle = model.hackTitle
        this.hackDescription = model.hackDescription
        this.publishDate = model.publishDate
        this.source = model.source
        this.date = model.date
        this.registration = model.registration
        this.focus = model.focus
        this.prize = model.prize
        this.routes = model.routes
        this.terms = model.terms
        this.organization = model.organization
        this.imageUrl = model.imageUrl
    }
}
