package ru.boringowl.myroadmapapp.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.boringowl.myroadmapapp.model.*

interface UserApi {
    @GET(ConstantsServer.authEndpoint)
    suspend fun me() : User

    @POST("${ConstantsServer.authEndpoint}/login")
    suspend fun auth(@Body creds: LoginData) :
            UserTokenData

    @POST("${ConstantsServer.authEndpoint}/register")
    suspend fun register(@Body userData: RegisterData) :
            UserTokenData

    @POST("${ConstantsServer.authEndpoint}/resetPassword")
    suspend fun resetPassword(@Body resetData: RestorePasswordData) :
            String

}