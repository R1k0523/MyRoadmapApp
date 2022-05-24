package ru.boringowl.myroadmapapp.presentation.features.todos.list

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
import ru.boringowl.myroadmapapp.data.room.repos.TodoRepository
import ru.boringowl.myroadmapapp.model.Route
import ru.boringowl.myroadmapapp.model.Skill
import ru.boringowl.myroadmapapp.model.Todo
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val repository: TodoRepository,
) : ViewModel() {

    private var _modelList = MutableStateFlow<List<Todo>>(emptyList())
    val modelList = _modelList.asStateFlow()

    var isSearchOpened by mutableStateOf(false)
    var searchText by mutableStateOf("")
    fun fetchTodos() {
        launchIO {
            try {
                repository.synchronizeData()
                repository.get().distinctUntilChanged().collect { _modelList.value = it }
            } catch (e: Exception) {}
        }
    }
    fun filteredIsEmpty() = modelList.value.none { isFiltered(it) }
    fun isFiltered(model: Todo?) = model != null && model.header.lowercase().contains(searchText)
    fun update(model: Todo) = launchIO { repository.update(model) }
    fun delete(model: Todo) = launchIO { repository.delete(model) }
    fun delete() = launchIO { repository.delete() }
}

