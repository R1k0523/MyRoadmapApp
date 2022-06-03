package ru.boringowl.myroadmapapp.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.boringowl.myroadmapapp.data.network.ConstantsServer
import ru.boringowl.myroadmapapp.data.network.dto.ListResponse
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