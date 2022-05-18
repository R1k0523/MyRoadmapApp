package ru.boringowl.myroadmapapp.presentation.repository.room.repos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.presentation.repository.room.dao.HackathonDao
import ru.boringowl.myroadmapapp.presentation.repository.room.model.HackathonEntity
import javax.inject.Inject

class HackathonRepository @Inject constructor(private val dao: HackathonDao) {

    private fun entity(model: Hackathon) = HackathonEntity(model)

    suspend fun add(model: Hackathon) = dao.insert(entity(model))
    suspend fun update(model: Hackathon) = dao.update(entity(model))
    suspend fun delete(model: Hackathon) = dao.delete(entity(model))
    suspend fun deleteAll() = dao.delete()
    fun getAllNotes(): Flow<List<Hackathon>> = dao.get().map{ f -> f.map { it.toModel() } }.flowOn(Dispatchers.IO).conflate()

}