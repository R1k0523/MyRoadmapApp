package ru.boringowl.myroadmapapp.presentation.features.hackathons.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.boringowl.myroadmapapp.data.room.repos.HackathonRepository
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class HackathonViewModel @Inject constructor(
    private val repository: HackathonRepository
) : ViewModel() {

    var modelList: Flow<PagingData<Hackathon>> = flow { PagingData.empty<Hackathon>() }
    var isSearchOpened by mutableStateOf(false)
    var searchText by mutableStateOf("")
    init {
        launchIO {
            modelList = repository.get()
        }
    }

    fun fetchAndSave() = launchIO {
        repository.fetchAndSave()
    }
    fun isFiltered(hackathon: Hackathon?) = hackathon != null && hackathon.fullText().contains(searchText)
    fun add(model: Hackathon) = launchIO { repository.add(model) }
    fun update(model: Hackathon) = launchIO { repository.update(model) }
    fun delete(model: Hackathon) = launchIO { repository.delete(model) }
    fun delete() = launchIO { repository.delete() }
}

