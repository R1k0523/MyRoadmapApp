package ru.boringowl.myroadmapapp.presentation.features.todos.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.boringowl.myroadmapapp.data.room.repos.TodoSkillRepository
import ru.boringowl.myroadmapapp.model.SkillTodo
import ru.boringowl.myroadmapapp.model.Todo
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoSkillRepository,
) : ViewModel() {

    private var todoId: UUID? = null
    private var _model = MutableStateFlow<Todo?>(null)
    val model = _model.asStateFlow()
    var loading by mutableStateOf(false)
    var searchText by mutableStateOf("")
    var filterOpened by mutableStateOf(false)
    var sortTypes by mutableStateOf(SortTodosBy.NECESSITY)

    fun sort(list: List<SkillTodo>) = when (sortTypes) {
        SortTodosBy.NAME -> list.sortedBy { it.skillName }
        SortTodosBy.NECESSITY -> list.sortedBy { it.necessity }.reversed()
        SortTodosBy.PROGRESS -> list.sortedBy { it.progress }.reversed()
    }

    fun fetchTodoSkills(todoId: UUID) {
        this.todoId = todoId
        launchIO {
            repository.get(todoId).distinctUntilChanged().collect { todo ->
                _model.value = todo?.apply { skills = skills?.let { sort(it) } }
            }
        }
        launchIO {
            loading = true
            repository.synchronizeData(todoId)
            loading = false
        }
    }


    fun update(model: SkillTodo) = launchIO {
        repository.update(model.apply {
            this.todo = todoId?.let { Todo(it) }
        })
    }

    fun delete(model: SkillTodo) = launchIO { repository.delete(model) }
    fun delete() = launchIO { repository.delete() }
}

enum class SortTodosBy(val displayName: String) {
    NAME("По имени"), NECESSITY("По важности"), PROGRESS("По готовности")
}