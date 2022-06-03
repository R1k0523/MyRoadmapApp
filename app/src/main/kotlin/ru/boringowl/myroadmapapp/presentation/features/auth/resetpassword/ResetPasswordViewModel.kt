package ru.boringowl.myroadmapapp.presentation.features.auth.resetpassword

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.boringowl.myroadmapapp.data.network.dto.RestorePasswordData
import ru.boringowl.myroadmapapp.data.room.repos.UserRepository
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var state: MutableState<ResetPasswordState> = mutableStateOf(ResetPasswordState.Default())

    fun resetPassword() {
        viewModelScope.launch {
            state.value = ResetPasswordState.Loading()
            userRepository.resetPassword(
                RestorePasswordData(
                    username,
                    email
                ),
                onSuccess = { state.value = ResetPasswordState.Success() },
                onError = { state.value = ResetPasswordState.Error(it) },
            )
        }
    }

    fun isUIEnabled() = state.value is ResetPasswordState.Default || isError()
    fun isError() = state.value is ResetPasswordState.Error
    fun setNewUsername(name: String) {
        username = name
        state.value = ResetPasswordState.Default()
    }
    fun setNewEmail(mail: String) {
        email = mail
        state.value = ResetPasswordState.Default()
    }
}

sealed class ResetPasswordState() {
    class Default : ResetPasswordState()
    class Loading : ResetPasswordState()
    class Error(val error: String = "") : ResetPasswordState()
    class Success : ResetPasswordState()
}