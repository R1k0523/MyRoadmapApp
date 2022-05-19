package ru.boringowl.myroadmapapp.data.network

import androidx.room.Query
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.model.ListResponse

interface HackApi {

    @GET("api/hack")
    suspend fun fetch(@Header("Authorization") auth: String = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVXNlciIsInN1YiI6IkJvcmluZ093bCIsImlhdCI6MTY1Mjk2NjA5NSwiZXhwIjoxNjUzMDUyNDk1fQ.US5ZdCf0dw6o4zcDWPdbCp-U4tc_wocH_uS81uKFtZFRMBivqMHNzqcF8gA01ymdxBt_DdJbBk1Q-huZw-zgQw") : ListResponse<Hackathon>

}