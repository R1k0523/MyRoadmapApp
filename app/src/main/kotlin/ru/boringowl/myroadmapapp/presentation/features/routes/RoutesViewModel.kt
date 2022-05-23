package ru.boringowl.myroadmapapp.presentation.features.routes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import ru.boringowl.myroadmapapp.data.room.repos.HackathonRepository
import ru.boringowl.myroadmapapp.data.room.repos.RouteRepository
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.model.Route
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val repository: RouteRepository
) : ViewModel() {

    private var _modelList = MutableStateFlow<List<Route>>(emptyList())
    val modelList = _modelList.asStateFlow()

    var isSearchOpened by mutableStateOf(false)
    var searchText by mutableStateOf("")
    init {
        launchIO {
            repository.get().distinctUntilChanged().collect {  _modelList.value = it }
            repository.fetchAndSave()
        }
    }
    fun isFiltered(model: Route?) = model != null && model.routeName.lowercase().contains(searchText)
    fun add(model: Route) = launchIO { repository.add(model) }
    fun update(model: Route) = launchIO { repository.update(model) }
    fun delete(model: Route) = launchIO { repository.delete(model) }
    fun delete() = launchIO { repository.delete() }
}

