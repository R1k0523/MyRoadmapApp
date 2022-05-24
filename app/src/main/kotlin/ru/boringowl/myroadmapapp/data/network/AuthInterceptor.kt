package ru.boringowl.myroadmapapp.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response
import ru.boringowl.myroadmapapp.data.datastore.DataStorage
import ru.boringowl.myroadmapapp.data.room.dao.UserDao
import ru.boringowl.myroadmapapp.data.room.repos.UserRepository

class AuthInterceptor(val dataStore: DataStorage, val dao: UserDao) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = runBlocking {dataStore.authToken().first() }
        if (token.isNotEmpty())
        requestBuilder.addHeader("Authorization", "Bearer $token")

        val response = chain.proceed(requestBuilder.build())
        if (response.code == 403) {
             runBlocking{
                 launch {
                     withContext(Dispatchers.IO){
                         dataStore.deleteToken()
                         dao.delete()
                     }
                 }
             }
        }
        return response
    }
}