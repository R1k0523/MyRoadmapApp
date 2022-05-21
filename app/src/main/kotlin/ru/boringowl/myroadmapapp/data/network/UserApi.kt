package ru.boringowl.myroadmapapp.data.network

import android.service.autofill.UserData
import retrofit2.http.*
import ru.boringowl.myroadmapapp.model.*

interface UserApi {
    @GET(ConstantsServer.authEndpoint)
    suspend fun me() : User

    @GET("${ConstantsServer.todoEndpoint}/login")
    suspend fun auth(@Body creds: LoginData) :
            UserTokenData

    @POST("${ConstantsServer.todoEndpoint}/register")
    suspend fun register(@Body userData: UserData) :
            UserTokenData

    @POST("${ConstantsServer.todoEndpoint}/resetPassword")
    suspend fun resetPassword(@Body resetData: ResetPasswordData) :
            String

}