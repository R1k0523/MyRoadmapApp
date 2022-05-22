package ru.boringowl.myroadmapapp.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.launchIO( toDo: suspend () -> Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        toDo()
    }
}