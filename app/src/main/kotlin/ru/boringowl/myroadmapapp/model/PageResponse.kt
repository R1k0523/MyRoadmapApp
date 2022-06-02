package ru.boringowl.myroadmapapp.model

class PageResponse<T>(
    val content: List<T>,
    val totalPages: Int,
    val number: Int,
    val last: Boolean,
    val first: Boolean,
    val empty: Boolean,
)