package ru.boringowl.myroadmapapp.presentation.features.auth.signup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.boringowl.myroadmapapp.data.room.repos.UserRepository
import ru.boringowl.myroadmapapp.model.RegisterData
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordAgain by mutableStateOf("")
    var email by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var passwordAgainVisible by mutableStateOf(false)
    var state: MutableState<SignUpState> = mutableStateOf(SignUpState.Default())

    fun signUp() {
        viewModelScope.launch {
            state.value = SignUpState.Loading()
            userRepository.register(
                RegisterData(
                    username,
                    password,
                    email
                ),
                onSuccess = { state.value = SignUpState.Success() },
                onError = { state.value = SignUpState.Error(it) },
            )
        }
    }

    fun isUIEnabled() = state.value is SignUpState.Default || isError()
    fun isError() = state.value is SignUpState.Error
    fun setNewPassword(pass: String) {
        password = pass
        state.value = SignUpState.Default()
    }
    fun setNewUsername(name: String) {
        username = name
        state.value = SignUpState.Default()
    }
    fun setAgainPassword(pass: String) {
        passwordAgain = pass
        state.value = SignUpState.Default()
    }
    fun setNewEmail(mail: String) {
        email = mail
        state.value = SignUpState.Default()
    }
    fun passwordsAreSimilar() = password == passwordAgain
}

sealed class SignUpState {
    class Default : SignUpState()
    class Loading : SignUpState()
    class Error(val error: String = "") : SignUpState()
    class Success : SignUpState()
}