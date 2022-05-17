package ru.boringowl.myroadmapapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.boringowl.myroadmapapp.model.Todo
import javax.inject.Inject

@HiltViewModel
class SampleListViewModel @Inject constructor() : ViewModel() {
    private val _todos = MutableStateFlow(listOf<Todo>())
    val todos get() = _todos.asStateFlow()

    operator fun plusAssign(todo: Todo) {
        _todos.update { it + todo }
    }
}