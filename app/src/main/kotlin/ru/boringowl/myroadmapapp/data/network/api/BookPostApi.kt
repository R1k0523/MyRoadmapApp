package ru.boringowl.myroadmapapp.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.boringowl.myroadmapapp.data.network.ConstantsServer
import ru.boringowl.myroadmapapp.data.network.dto.PageResponse
import ru.boringowl.myroadmapapp.model.BookPost
import java.util.*

interface BookPostApi {
    @GET("${ConstantsServer.booksEndpoint}/route/{id}")
    suspend fun get(
        @Path("id") id: Int,
        @Query("page") page: Int = 0,
        @Query("limit") limit: Int = 20,
        @Query("query") query: String = ""
    ): PageResponse<BookPost>

    @GET("${ConstantsServer.booksEndpoint}/{id}")
    suspend fun get(
        @Path("id") id: UUID
    ): BookPost
}