package ru.boringowl.myroadmapapp.data.network

import retrofit2.http.*
import ru.boringowl.myroadmapapp.model.ListResponse
import ru.boringowl.myroadmapapp.model.Route
import java.util.*

interface RouteApi {
    @GET(ConstantsServer.routeEndpoint)
    suspend fun get() :
            ListResponse<Route>

    @GET("${ConstantsServer.routeEndpoint}/{id}")
    suspend fun get(@Path("id") id: UUID) :
            Route
}