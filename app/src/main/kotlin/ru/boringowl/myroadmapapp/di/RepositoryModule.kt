package ru.boringowl.myroadmapapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.boringowl.myroadmapapp.data.datastore.DataStorage
import ru.boringowl.myroadmapapp.data.datastore.DataStorageImpl
import ru.boringowl.myroadmapapp.data.network.*
import ru.boringowl.myroadmapapp.data.room.AppDatabase
import ru.boringowl.myroadmapapp.data.room.dao.*
import ru.boringowl.myroadmapapp.data.room.repos.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton @Provides
    fun provideHackathonRepository(dao: HackathonDao, api: HackApi, db: AppDatabase) = HackathonRepository(dao, api, db)
    @Singleton @Provides
    fun provideUserRepository(dao: UserDao, api: UserApi, ds: DataStorage, db: AppDatabase) = UserRepository(dao, api, ds, db)
    @Singleton @Provides
    fun provideRouteRepository(dao: RouteDao, api: RouteApi) = RouteRepository(dao, api)
    @Singleton @Provides
    fun provideTodoRepository(dao: TodoDao, repo: TodoSkillRepository, api: TodoApi) = TodoRepository(dao, repo, api)
    @Singleton @Provides
    fun provideTodoSkillRepository(dao: SkillTodoDao ,todoDao: TodoDao, api: SkillTodoApi) = TodoSkillRepository(dao, todoDao, api)
    @Singleton @Provides
    fun provideSkillRepository(dao: SkillDao, routeDao: RouteDao, api: SkillApi) = SkillRepository(dao, routeDao, api)
    @Singleton @Provides
    fun provideBooksRepository(dao: BookPostDao, api: BookPostApi, db: AppDatabase) = BooksRepository(dao, api, db)
    @Singleton @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStorage = DataStorageImpl(context)


}