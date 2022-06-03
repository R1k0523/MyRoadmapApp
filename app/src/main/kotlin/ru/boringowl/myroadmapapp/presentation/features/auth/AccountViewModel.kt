package ru.boringowl.myroadmapapp.presentation.features.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.boringowl.myroadmapapp.data.datastore.DataStorage
import ru.boringowl.myroadmapapp.data.room.repos.UserRepository
import ru.boringowl.myroadmapapp.model.User
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    val repository: UserRepository,
    val dataStorage: DataStorage
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    fun fetchUser() {
        launchIO { repository.get().distinctUntilChanged().collect { _currentUser.value = it } }
        launchIO { repository.fetchMe() }
    }

    fun logOut() = launchIO { repository.logout() }
}