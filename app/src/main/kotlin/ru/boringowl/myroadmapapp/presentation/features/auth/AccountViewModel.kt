package ru.boringowl.myroadmapapp.presentation.features.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.boringowl.myroadmapapp.data.datastore.DataStorage
import ru.boringowl.myroadmapapp.data.room.repos.UserRepository
import ru.boringowl.myroadmapapp.model.User
import ru.boringowl.myroadmapapp.presentation.base.Themes
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    val repository: UserRepository,
    val dataStorage: DataStorage
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()
    val _theme = MutableStateFlow(Themes.SYSTEM.name)
    val theme = _theme.asStateFlow()
    fun fetchUser() {
        fetchTheme()
        launchIO { repository.get().distinctUntilChanged().collect { _currentUser.value = it } }
        launchIO { repository.fetchMe() }

    }
    fun fetchTheme() = launchIO { dataStorage.selectedTheme().collect { _theme.value = it }}
    fun setTheme(theme: Themes) = launchIO {
        dataStorage.setSelectedTheme(theme.name)
    }

    fun logOut() = launchIO { repository.logout() }
}