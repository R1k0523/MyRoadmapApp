package ru.boringowl.myroadmapapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.boringowl.myroadmapapp.model.Hackathon
import ru.boringowl.myroadmapapp.presentation.repository.room.repos.HackathonRepository
import javax.inject.Inject

@HiltViewModel
class HackathonViewModel @Inject constructor(private val repository: HackathonRepository) : ViewModel() {

    private val _modelList = MutableStateFlow<List<Hackathon>>(emptyList())
    val modelList = _modelList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllNotes().distinctUntilChanged()
                .collect { l ->
                     if (l.isEmpty()) {
                         Log.d("Empty", ": Empty list")
                     }else {
                         _modelList.value = l
                     }

                }

        }
    }

     fun add(model: Hackathon) = viewModelScope.launch { repository.add(model) }
     fun update(model: Hackathon) = viewModelScope.launch { repository.update(model) }
     fun remove(model: Hackathon) = viewModelScope.launch { repository.delete(model) }

}