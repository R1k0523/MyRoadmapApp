package ru.boringowl.myroadmapapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.boringowl.myroadmapapp.BuildConfig
import ru.boringowl.myroadmapapp.data.network.HackApi
import ru.boringowl.myroadmapapp.data.room.dao.HackathonDao
import ru.boringowl.myroadmapapp.data.room.repos.HackathonRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.MAIN_LINK)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Singleton @Provides
    fun okhttpClient() : OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
    @Singleton @Provides
    fun provideHackApi(client: Retrofit): HackApi = client.create(HackApi::class.java)

}