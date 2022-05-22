package ru.boringowl.myroadmapapp.data.network

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.boringowl.myroadmapapp.data.datastore.DataStorage

class AuthInterceptor(val dataStore: DataStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = runBlocking {dataStore.authToken().first() }
        if (token.isNotEmpty())
        requestBuilder.addHeader("Authorization", "Bearer $token")
        return chain.proceed(requestBuilder.build())
    }
}