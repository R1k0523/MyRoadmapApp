package ru.boringowl.myroadmapapp.presentation.features.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.boringowl.myroadmapapp.data.network.dto.UpdatePasswordData
import ru.boringowl.myroadmapapp.data.network.dto.UserEmailData
import ru.boringowl.myroadmapapp.data.room.repos.UserRepository
import ru.boringowl.myroadmapapp.model.User
import ru.boringowl.myroadmapapp.presentation.base.launchIO
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val user: MutableState<User?> = mutableStateOf(null)
    var state: MutableState<ProfileState> = mutableStateOf(ProfileState.Default)
    var isPasswordChanging by mutableStateOf(false)
    var isEmailChanging by mutableStateOf(false)
    var passwordVisible by mutableStateOf(false)
    var newPasswordVisible by mutableStateOf(false)
    var passwordText by mutableStateOf("")
    var usernameText by mutableStateOf("")
    var newPasswordText by mutableStateOf("")
    var emailText by mutableStateOf("")

    fun fetch() {
        launchIO {
            repository.get().distinctUntilChanged().collect { u ->
                user.value = u
                u?.let {
                    emailText = u.email
                    usernameText = u.username
                }
            }
        }
        launchIO { repository.fetchMe() }
    }

    fun resetEmail() {
        emailText = user.value?.email ?: ""
    }
    fun updatePassword(onFinish: suspend () -> Unit) = launchIO {
        state.value = ProfileState.Loading
        repository.update(
            UpdatePasswordData(
            passwordText, newPasswordText
        ),
        onSuccess = {
            state.value = ProfileState.Success("???????????? ????????????????")
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
        state.value = ProfileState.Loading
        repository.update(
            UserEmailData(emailText),
            onSuccess = {
                state.value = ProfileState.Success("???????????????????? ??????????????????????")
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
        state.value = ProfileState.Default
    }
    fun setNewPassword(pass: String) {
        newPasswordText = pass
        state.value = ProfileState.Default
    }
}

sealed class ProfileState {
    object Default : ProfileState()
    object Loading : ProfileState()
    class Error(val error: String = "") : ProfileState()
    class Success(val text: String = "") : ProfileState()
}

