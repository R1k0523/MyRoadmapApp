package ru.boringowl.myroadmapapp.presentation.features.routes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.boringowl.myroadmapapp.data.room.repos.RouteRepository
import ru.boringowl.myroadmapapp.data.room.repos.TodoRepository
import ru.boringowl.myroadmapapp.model.Route
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val repository: RouteRepository,
    private val todoRepository: TodoRepository,
) : ViewModel() {
    private var _modelList = MutableStateFlow<List<Route>>(emptyList())
    val modelList = _modelList.asStateFlow()
    var onError: suspend (msg: String) -> Unit = {}
    var isSearchOpened by mutableStateOf(false)
    var isDialogOpened by mutableStateOf(false)
    var searchText by mutableStateOf("")
    var todoName by mutableStateOf("")
    var routeName by mutableStateOf("")
    var pickedRoute by mutableStateOf(-1)

    fun fetch() {
        launchIO {
            repository.get().distinctUntilChanged().collect {  _modelList.value = it }
        }
        launchIO {
            repository.fetchAndSave()
        }
    }

    fun filteredIsEmpty() = modelList.value.none { isFiltered(it) }
    fun isFiltered(model: Route?) = model != null && model.routeName.lowercase().contains(searchText)
    fun add(
        onSuccess: suspend () -> Unit,
        onError: suspend (msg: String) -> Unit,
    ) = launchIO {
        todoRepository.add(pickedRoute, "$todoName ($routeName)",
            onError = { onError("Сетевая ошибка") },
            onSuccess = onSuccess
        )
    }
    fun delete() = launchIO { repository.delete() }
}

