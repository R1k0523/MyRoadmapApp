package ru.boringowl.myroadmapapp.presentation.features.books

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
import ru.boringowl.myroadmapapp.data.room.repos.BooksRepository
import ru.boringowl.myroadmapapp.model.BookPost
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repository: BooksRepository
) : ViewModel() {
    var state: MutableState<BooksState> = mutableStateOf(BooksState.Loading)
    var modelList by mutableStateOf<Flow<PagingData<BookPost>>> (flow { PagingData.empty<Hackathon>() })
    var isSearchOpened by mutableStateOf(false)
    private var job: Job? = null
    var searchText by mutableStateOf("")
        private set

    fun fetch(routeId: Int) {
        if (job != null) job?.cancel()
        job = launchIO {
            modelList = repository.get(routeId, searchText).distinctUntilChanged().cachedIn(viewModelScope)
            delay(1000)
            state.value = BooksState.Default
        }
    }
    fun updateSearch(text: String, routeId: Int) {
        state.value = BooksState.Loading
        searchText = text
        fetch(routeId)
    }
}


sealed class BooksState {
    object Default : BooksState()
    object Loading : BooksState()
    class Error(val error: String = "") : BooksState()
    class Success(val text: String = "") : BooksState()
}

