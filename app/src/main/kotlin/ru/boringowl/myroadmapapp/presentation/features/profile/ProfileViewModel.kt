package ru.boringowl.myroadmapapp.presentation.features.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.boringowl.myroadmapapp.data.room.repos.HackathonRepository
import ru.boringowl.myroadmapapp.data.room.repos.RouteRepository
import ru.boringowl.myroadmapapp.data.room.repos.UserRepository
import ru.boringowl.myroadmapapp.model.*
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val user: MutableState<User?> = mutableStateOf(null)
    var state: MutableState<ProfileState> = mutableStateOf(ProfileState.Default())
    var isPasswordChanging by mutableStateOf(false)
    var isEmailChanging by mutableStateOf(false)
    var passwordVisible by mutableStateOf(false)
    var newPasswordVisible by mutableStateOf(false)
    var passwordText by mutableStateOf("")
    var usernameText by mutableStateOf("")
    var newPasswordText by mutableStateOf("")
    var emailText by mutableStateOf("")
    init {
        launchIO {
            repository.get().distinctUntilChanged().collect { u ->
                u?.let {
                    emailText = u.email
                    usernameText = u.username
                }
                user.value = u
            }
            repository.fetchMe()
        }
    }

    fun resetEmail() {
        emailText = user.value?.email ?: ""
    }
    fun updatePassword(onFinish: suspend () -> Unit) = launchIO {
        state.value = ProfileState.Loading()
        repository.update(UpdatePasswordData(
            passwordText, newPasswordText
        ),
        onSuccess = {
            state.value = ProfileState.Success("Пароль обновлен")
            passwordText = ""
            newPasswordText = ""
            isPasswordChanging = false
            onFinish()
        },
        onError = {
            state.value = ProfileState.Error(it)
            onFinish()
        })
    }
    fun update(onFinish: suspend () -> Unit) = launchIO {
        state.value = ProfileState.Loading()
        repository.update(
            UserEmailData(emailText),
            onSuccess = {
                state.value = ProfileState.Success("Сохранение произведено")
                isEmailChanging = false
                onFinish()
            },
            onError = {
                state.value = ProfileState.Error(it)
                onFinish()
            })
    }



    fun isUIEnabled() = state.value is ProfileState.Default || state.value is ProfileState.Success || isError()
    fun isError() = state.value is ProfileState.Error
    fun snackBarText() = when(state.value) {
        is ProfileState.Success -> (state.value as ProfileState.Success).text
        is ProfileState.Error -> (state.value as ProfileState.Error).error
        else -> ""
    }
    fun setPassword(pass: String) {
        passwordText = pass
        state.value = ProfileState.Default()
    }
    fun setNewPassword(pass: String) {
        newPasswordText = pass
        state.value = ProfileState.Default()
    }
}

sealed class ProfileState() {
    class Default : ProfileState()
    class Loading : ProfileState()
    class Error(val error: String = "") : ProfileState()
    class Success(val text: String = "") : ProfileState()
}

