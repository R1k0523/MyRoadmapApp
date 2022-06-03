package ru.boringowl.myroadmapapp.presentation.features.hackathons

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import ru.boringowl.myroadmapapp.data.room.repos.HackathonRepository
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class HackathonViewModel @Inject constructor(
    private val repository: HackathonRepository
) : ViewModel() {
    var state: MutableState<HackthonState> = mutableStateOf(HackthonState.Loading())
    var modelList by mutableStateOf<Flow<PagingData<Hackathon>>> (flow { PagingData.empty<Hackathon>() })
    var isSearchOpened by mutableStateOf(false)
    var searchText by mutableStateOf("")
        private set
    var job: Job? = null
    fun fetch() {
        if (job != null) job?.cancel()
        job = launchIO {
            modelList = repository.get(searchText).distinctUntilChanged().cachedIn(viewModelScope)
            delay(1000)
            state.value = HackthonState.Default()
        }
    }
    fun updateSearch(text: String) {
        state.value = HackthonState.Loading()
        searchText = text
        fetch()
    }
    fun isLoading() = state.value is HackthonState.Loading
}


sealed class HackthonState() {
    class Default : HackthonState()
    class Loading : HackthonState()
    class Empty : HackthonState()
    class Error(val error: String = "") : HackthonState()
    class Success(val text: String = "") : HackthonState()
}

