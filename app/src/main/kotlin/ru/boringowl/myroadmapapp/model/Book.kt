package ru.boringowl.myroadmapapp.model

import ru.boringowl.myroadmapapp.presentation.base.format
import java.util.*

class BookPost(
    var bookPostId: UUID? = null,
    var routeId: Int? = null,
    var description: String = "",
    var books: List<BookInfo> = listOf()
)

class BookInfo (
    var url: String = "",
    var filename: String = "",
    var size: Int = 0


) {
    fun sizeString(): String {
        val sizeKb = size.toDouble() / 1000
        return if (sizeKb > 1000) "${(sizeKb / 1000).format(2)} МБайт"
        else "${sizeKb.format(2)} КБайт"
    }
}
