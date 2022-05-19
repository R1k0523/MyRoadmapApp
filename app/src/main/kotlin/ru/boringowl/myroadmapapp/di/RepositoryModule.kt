package ru.boringowl.myroadmapapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.boringowl.myroadmapapp.data.network.HackApi
import ru.boringowl.myroadmapapp.data.room.dao.HackathonDao
import ru.boringowl.myroadmapapp.data.room.repos.HackathonRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton @Provides
    fun provideHackathonRepository(dao: HackathonDao, api: HackApi) = HackathonRepository(dao, api)

}