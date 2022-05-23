package ru.boringowl.myroadmapapp.model


class Route(var routeId: Int? = null, var routeName: String = "", var routeDescription: String = "") {
    var resumesCount: Int = 0
    var vacanciesCount: Int = 0

    fun index() = resumesCount / vacanciesCount
}
