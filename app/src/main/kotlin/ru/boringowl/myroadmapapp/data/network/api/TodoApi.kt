package ru.boringowl.myroadmapapp.data.network.api

import retrofit2.http.*
import ru.boringowl.myroadmapapp.data.network.ConstantsServer
import ru.boringowl.myroadmapapp.data.network.dto.ListResponse
import ru.boringowl.myroadmapapp.model.Todo
import java.util.*

interface TodoApi {
    @GET(ConstantsServer.todoEndpoint)
    suspend fun get() :
            ListResponse<Todo>

    @GET("${ConstantsServer.todoEndpoint}/{id}")
    suspend fun get(@Path("id") id: UUID) :
            Todo

    @POST("${ConstantsServer.todoEndpoint}/{id}")
    suspend fun add(@Path("id") id: Int, @Query("name") name: String) :
            Todo

    @PUT(ConstantsServer.todoEndpoint)
    suspend fun update(@Body model: Todo) :
            Todo

    @DELETE("${ConstantsServer.todoEndpoint}/{id}")
    suspend fun delete(@Path("id") id: UUID)

}