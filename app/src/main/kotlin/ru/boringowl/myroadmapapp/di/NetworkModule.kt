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
import ru.boringowl.myroadmapapp.data.datastore.DataStorage
import ru.boringowl.myroadmapapp.data.network.AuthInterceptor
import ru.boringowl.myroadmapapp.data.network.api.*
import ru.boringowl.myroadmapapp.data.room.dao.UserDao
import java.util.concurrent.TimeUnit
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
    fun provideHackApi(client: Retrofit): HackApi = client.create(HackApi::class.java)
    @Singleton @Provides
    fun provideRouteApi(client: Retrofit): RouteApi = client.create(RouteApi::class.java)
    @Singleton @Provides
    fun provideSkillApi(client: Retrofit): SkillApi = client.create(SkillApi::class.java)
    @Singleton @Provides
    fun provideSkillTodoApi(client: Retrofit): SkillTodoApi = client.create(SkillTodoApi::class.java)
    @Singleton @Provides
    fun provideTodoApi(client: Retrofit): TodoApi = client.create(TodoApi::class.java)
    @Singleton @Provides
    fun provideUserApi(client: Retrofit): UserApi = client.create(UserApi::class.java)
    @Singleton @Provides
    fun provideBooksApi(client: Retrofit): BookPostApi = client.create(BookPostApi::class.java)

    @Singleton @Provides
    fun okhttpClient(dataStorage: DataStorage, dao: UserDao) : OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(AuthInterceptor(dataStorage, dao))
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}