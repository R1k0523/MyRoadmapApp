package ru.boringowl.myroadmapapp.model

import java.time.LocalDateTime
import java.util.*

class Hackathon {
    var hackId: UUID? = null
    var hackTitle: String = ""
    var hackDescription: String = ""
    var publishDate: LocalDateTime = LocalDateTime.now()
    var source: String = ""
    var date: String? = ""
    var registration: String? = ""
    var focus: String? = ""
    var prize: String? = ""
    var routes: String? = ""
    var terms: String? = ""
    var organization: String? = ""
    var imageUrl: String? = ""
}
