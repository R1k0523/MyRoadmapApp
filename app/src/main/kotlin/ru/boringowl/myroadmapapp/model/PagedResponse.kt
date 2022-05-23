package ru.boringowl.myroadmapapp.model

class PagedResponse<T>(val items: List<T>, val prevPage: Int?, val nextPage: Int?) {
    constructor(triple: Triple<List<T>, Int?, Int?>) : this(triple.first, triple.second, triple.third)
}
