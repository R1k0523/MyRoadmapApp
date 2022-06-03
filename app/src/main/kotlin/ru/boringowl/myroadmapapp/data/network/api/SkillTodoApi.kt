package ru.boringowl.myroadmapapp.data.network.api

import retrofit2.http.*
import ru.boringowl.myroadmapapp.data.network.ConstantsServer
import ru.boringowl.myroadmapapp.data.network.dto.ListResponse
import ru.boringowl.myroadmapapp.model.SkillTodo
import java.util.*

interface SkillTodoApi {
    @GET("${ConstantsServer.skillTodoEndpoint}/todo/{todoId}")
    suspend fun getByTodo(@Path("todoId") todoId: UUID) :
            ListResponse<SkillTodo>

    @GET("${ConstantsServer.skillTodoEndpoint}/{skillTodoId}")
    suspend fun get(@Path("skillTodoId") skillTodoId: UUID) :
            SkillTodo

    @POST(ConstantsServer.skillTodoEndpoint)
    suspend fun add(@Body model: SkillTodo) :
            SkillTodo

    @PUT(ConstantsServer.skillTodoEndpoint)
    suspend fun update(@Body model: SkillTodo) :
            SkillTodo

    @DELETE("${ConstantsServer.skillTodoEndpoint}/{id}")
    suspend fun delete(@Path("id") id: UUID) :
            String

}