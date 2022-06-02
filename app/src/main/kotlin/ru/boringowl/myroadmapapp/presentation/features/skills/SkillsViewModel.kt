package ru.boringowl.myroadmapapp.presentation.features.skills

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.boringowl.myroadmapapp.data.room.repos.RouteRepository
import ru.boringowl.myroadmapapp.data.room.repos.SkillRepository
import ru.boringowl.myroadmapapp.model.Route
import ru.boringowl.myroadmapapp.model.Skill
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class SkillsViewModel @Inject constructor(
    private val repository: SkillRepository,
) : ViewModel() {

    private var _modelList = MutableStateFlow<List<Skill>>(emptyList())
    val modelList = _modelList.asStateFlow()

    var isSearchOpened by mutableStateOf(false)
    var searchText by mutableStateOf("")
    fun fetchSkills(routeId: Int) {
        launchIO { repository.get(routeId).distinctUntilChanged().collect { _modelList.value = it } }
        launchIO { repository.fetchAndSave(routeId) }
    }
    fun filteredIsEmpty() = modelList.value.none { isFiltered(it) }
    fun isFiltered(model: Skill?) = model != null && model.skillName.lowercase().contains(searchText)
    fun add(model: Skill) = launchIO { repository.add(model) }
    fun update(model: Skill) = launchIO { repository.update(model) }
    fun delete(model: Skill) = launchIO { repository.delete(model) }
    fun delete() = launchIO { repository.delete() }
}

