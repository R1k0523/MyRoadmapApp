package ru.boringowl.myroadmapapp.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.model.ListResponse
import ru.boringowl.myroadmapapp.model.PagedResponse
import java.util.*

interface HackApi {
    @GET(ConstantsServer.hackEndpoint)
    suspend fun get(
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 100,
    ) :
            PagedResponse<Hackathon>

    @GET(ConstantsServer.hackEndpoint)
    suspend fun search(
        @Query("text") text: Int = 1
    ) :
            PagedResponse<Hackathon>

    @GET("${ConstantsServer.hackEndpoint}/{id}")
    suspend fun get(@Path("id") id: UUID) :
            Hackathon
}