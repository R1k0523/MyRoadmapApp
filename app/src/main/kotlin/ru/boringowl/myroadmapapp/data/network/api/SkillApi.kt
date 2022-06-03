package ru.boringowl.myroadmapapp.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.boringowl.myroadmapapp.data.network.ConstantsServer
import ru.boringowl.myroadmapapp.data.network.dto.ListResponse
import ru.boringowl.myroadmapapp.model.Skill
import java.util.*

interface SkillApi {
    @GET("${ConstantsServer.skillEndpoint}/route/{id}")
    suspend fun getByRoute(@Path("id") id: Int) :
            ListResponse<Skill>

    @GET("${ConstantsServer.skillEndpoint}/{id}")
    suspend fun get(@Path("id") id: UUID
    ) : Skill
}