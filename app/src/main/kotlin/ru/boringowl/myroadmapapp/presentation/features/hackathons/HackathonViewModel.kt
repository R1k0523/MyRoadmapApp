package ru.boringowl.myroadmapapp.presentation.features.hackathons

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import ru.boringowl.myroadmapapp.data.room.repos.HackathonRepository
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.model.Route
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class HackathonViewModel @Inject constructor(
    private val repository: HackathonRepository
) : ViewModel() {

    private var _modelList = MutableStateFlow<List<Hackathon>>(emptyList())
    val modelList = _modelList.asStateFlow()

    var isSearchOpened by mutableStateOf(false)
    var searchText by mutableStateOf("")

    fun fetch() = launchIO {
        repository.get().distinctUntilChanged().collect { _modelList.value = it }
        repository.fetchAndSave()
    }

    fun fetchAndSave() = launchIO {
        repository.fetchAndSave()
    }
    fun filteredIsEmpty() = modelList.value.none { isFiltered(it) }
    fun isFiltered(model: Hackathon?) = model != null && model.fullText().contains(searchText)
    fun add(model: Hackathon) = launchIO { repository.add(model) }
    fun update(model: Hackathon) = launchIO { repository.update(model) }
    fun delete(model: Hackathon) = launchIO { repository.delete(model) }
    fun delete() = launchIO { repository.delete() }
}

