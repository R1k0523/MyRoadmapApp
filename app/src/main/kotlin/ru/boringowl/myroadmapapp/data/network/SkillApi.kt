package ru.boringowl.myroadmapapp.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.boringowl.myroadmapapp.model.ListResponse
import ru.boringowl.myroadmapapp.model.Skill
import java.util.*

interface SkillApi {
    @GET("${ConstantsServer.skillEndpoint}/route/{id}")
    suspend fun getByRoute(@Path("id") id: UUID,
                           @Query("page") page: Int = 1,
                           @Query("perPage") perPage: Int = 20) :
            ListResponse<Skill>

    @GET("${ConstantsServer.skillEndpoint}/{id}")
    suspend fun get(@Path("id") id: UUID
    ) : Skill
}