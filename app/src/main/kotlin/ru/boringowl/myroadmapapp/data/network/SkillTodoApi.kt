package ru.boringowl.myroadmapapp.data.network

import retrofit2.http.*
import ru.boringowl.myroadmapapp.model.SkillTodo
import ru.boringowl.myroadmapapp.model.ListResponse
import java.util.*

interface SkillTodoApi {
    @GET("${ConstantsServer.skillTodoEndpoint}/{todoId}")
    suspend fun get(@Path("todoId") todoId: UUID) :
            ListResponse<SkillTodo>

    @GET("${ConstantsServer.skillTodoEndpoint}/{skillId}/{todoId}")
    suspend fun get(@Path("skillId") skillId: UUID, @Path("todoId") todoId: UUID) :
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

    @PUT("${ConstantsServer.skillTodoEndpoint}/progress/{id}")
    suspend fun setState(@Path("id") id: UUID, @Query("progress") progress: Int) :
            SkillTodo
}