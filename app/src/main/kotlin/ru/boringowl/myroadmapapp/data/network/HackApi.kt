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
    suspend fun get() :
            ListResponse<Hackathon>


    @GET("${ConstantsServer.hackEndpoint}/{id}")
    suspend fun get(@Path("id") id: UUID) :
            Hackathon
}