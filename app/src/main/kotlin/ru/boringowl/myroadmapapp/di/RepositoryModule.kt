package ru.boringowl.myroadmapapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.boringowl.myroadmapapp.presentation.repository.room.dao.HackathonDao
import ru.boringowl.myroadmapapp.presentation.repository.room.repos.HackathonRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton @Provides
    fun provideHackathonRepository(dao: HackathonDao) = HackathonRepository(dao)



}