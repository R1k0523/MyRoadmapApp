package ru.boringowl.myroadmapapp.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.boringowl.myroadmapapp.model.Hackathon
import java.util.*

@Entity(tableName = "book_keys")
class BookPostRemoteKeys(
    @PrimaryKey
    @ColumnInfo(name="id")
    var id: UUID,
    @ColumnInfo(name="prev")
    var prev: Int? = null,
    @ColumnInfo(name="next")
    var next: Int? = null,
)
