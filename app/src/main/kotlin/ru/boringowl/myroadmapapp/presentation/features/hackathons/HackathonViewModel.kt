package ru.boringowl.myroadmapapp.presentation.features.hackathons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.boringowl.myroadmapapp.data.room.repos.HackathonRepository
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class HackathonViewModel @Inject constructor(
    private val repository: HackathonRepository
) : ViewModel() {

    private val _modelList = MutableStateFlow<List<Hackathon>>(emptyList())
    val modelList = _modelList.asStateFlow()

    init {
        launchIO {
            repository.get().distinctUntilChanged().collect {  _modelList.value = it }
        }
    }

    fun fetchAndSave(page: Int = 1, perPage: Int = 20) = launchIO { repository.fetchAndSave(page, perPage) }
    fun add(model: Hackathon) = launchIO { repository.add(model) }
    fun update(model: Hackathon) = launchIO { repository.update(model) }
    fun delete(model: Hackathon) = launchIO { repository.delete(model) }
    fun delete() = launchIO { repository.delete() }
}

