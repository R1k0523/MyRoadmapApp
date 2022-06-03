package ru.boringowl.myroadmapapp.model

import java.util.*

class Hackathon {
    var hackId: UUID? = null
    var hackTitle: String = ""
    var hackDescription: String = ""
    var publishDate: String = ""
    var source: String = ""
    var date: String? = ""
    var registration: String? = ""
    var focus: String? = ""
    var prize: String? = ""
    var routes: String? = ""
    var terms: String? = ""
    var organization: String? = ""
    var imageUrl: String? = ""

    fun fullText(): String =
        listOf(hackTitle,
                hackDescription,
                date,
                registration,
                focus,
                prize,
                routes,
                terms,
                organization,
                imageUrl,
        ).joinToString(" ").lowercase()
}
