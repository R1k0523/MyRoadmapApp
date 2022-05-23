package ru.boringowl.myroadmapapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.boringowl.myroadmapapp.data.room.AppDatabase
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton @Provides
    fun provideAppDB(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Singleton @Provides
    fun provideHackathonDao(db: AppDatabase) = db.hackDao()

    @Singleton @Provides
    fun provideRouteDao(db: AppDatabase) = db.routeDao()

    @Singleton @Provides
    fun provideSkillDao(db: AppDatabase) = db.skillDao()

    @Singleton @Provides
    fun provideSkillTodoDao(db: AppDatabase) = db.skillTodoDao()

    @Singleton @Provides
    fun provideTodoDao(db: AppDatabase) = db.todoDao()

    @Singleton @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()

    @Singleton @Provides
    fun provideSkillRemoteDao(db: AppDatabase) = db.skillRemoteDao()

    @Singleton @Provides
    fun provideHackathonRemoteDao(db: AppDatabase) = db.hackRemoteDao()
}