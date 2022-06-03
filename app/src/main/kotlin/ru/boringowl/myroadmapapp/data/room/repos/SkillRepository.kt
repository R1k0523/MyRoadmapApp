package ru.boringowl.myroadmapapp.data.room.repos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.boringowl.myroadmapapp.data.network.api.SkillApi
import ru.boringowl.myroadmapapp.data.room.dao.RouteDao
import ru.boringowl.myroadmapapp.data.room.dao.SkillDao
import ru.boringowl.myroadmapapp.data.room.model.SkillEntity
import ru.boringowl.myroadmapapp.model.Skill
import javax.inject.Inject


class SkillRepository @Inject constructor(
    private val dao: SkillDao,
    private val routeDao: RouteDao,
    private val api: SkillApi
) {

    private fun entity(model: Skill) = SkillEntity(model)

    suspend fun add(model: Skill) = dao.insert(entity(model))
    suspend fun update(model: Skill) = dao.update(entity(model))
    suspend fun delete(model: Skill) = dao.delete(entity(model))
    suspend fun delete() = dao.delete()

    fun get(routeId: Int): Flow<List<Skill>> {
        return dao.getByRoute(routeId)
            .flowOn(Dispatchers.IO).conflate()
            .map { f -> f.map { it.skill.toModel(it.route.toModel()) } }

    }

    suspend fun fetchAndSave(routeId: Int) = loadWithIO {
        val models = api.getByRoute(routeId).items
        models.forEach {
            it.apply { route = routeDao.get(routeId)?.toModel() }
            add(it)
        }
    }
}