package ru.boringowl.myroadmapapp.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.boringowl.myroadmapapp.model.BookInfo
import ru.boringowl.myroadmapapp.model.BookPost
import java.util.*

@Entity(tableName = "book_posts")
class BookPostEntity(
    @PrimaryKey
    @ColumnInfo(name="book_post_id")
    var bookPostId: UUID,
    @ColumnInfo(name="route_id")
    var routeId: Int? = null,
    @ColumnInfo(name="description")
    var description: String = "",
    @ColumnInfo(name="books")
    var books: List<BookInfo> = listOf()
) {

    fun toModel(): BookPost = BookPost().also {
        it.bookPostId = bookPostId
        it.routeId = routeId
        it.description = description
        it.books = books
    }

    constructor(model: BookPost) : this(
        model.bookPostId!!,
        model.routeId,
        model.description,
        model.books,
    )
}