package ru.boringowl.myroadmapapp.data.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import ru.boringowl.myroadmapapp.data.network.ConstantsServer
import ru.boringowl.myroadmapapp.data.network.dto.*
import ru.boringowl.myroadmapapp.model.User

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
    suspend fun resetPassword(@Body resetData: RestorePasswordData)

    @PUT(ConstantsServer.authEndpoint)
    suspend fun updateEmail(@Body userData: UserEmailData) : User

    @POST("${ConstantsServer.authEndpoint}/updatePassword")
    suspend fun updatePassword(@Body userData: UpdatePasswordData)

}