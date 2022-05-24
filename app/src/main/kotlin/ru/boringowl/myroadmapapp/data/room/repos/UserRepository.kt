package ru.boringowl.myroadmapapp.data.room.repos

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import ru.boringowl.myroadmapapp.data.datastore.DataStorage
import ru.boringowl.myroadmapapp.data.network.UserApi
import ru.boringowl.myroadmapapp.data.network.errorText
import ru.boringowl.myroadmapapp.data.room.dao.UserDao
import ru.boringowl.myroadmapapp.data.room.model.UserEntity
import ru.boringowl.myroadmapapp.model.*
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val dao: UserDao,
    private val api: UserApi,
    private val dataStorage: DataStorage
) {
    private var isLoading by mutableStateOf(false)

    private val dispUploader = DispUploader({ isLoading = true }, { isLoading = false })

    private fun entity(model: User) = UserEntity(model)

    suspend fun register(
        data: RegisterData,
        onSuccess: () -> Unit = {},
        onError: (message: String) -> Unit = {}
    ) = dispUploader.load {
        try {
            val token = api.register(data)
            dataStorage.setAuthToken(token.accessToken)
            fetchMe()
            onSuccess()
        } catch (e: HttpException) {
            onError(e.errorText())
        }
    }

    suspend fun login(
        data: LoginData,
        onSuccess: suspend () -> Unit = {},
        onError: suspend (message: String) -> Unit = {}
    ) = dispUploader.load {
        try {
            val token = api.auth(data)
            dataStorage.setAuthToken(token.accessToken)
            fetchMe()
            onSuccess()
        } catch (e: HttpException) {
            onError(e.errorText())
        }
    }

    suspend fun resetPassword(
        data: RestorePasswordData,
        onSuccess: suspend () -> Unit = {},
        onError: suspend (message: String) -> Unit = {}
    ) = dispUploader.load {
        try {
            api.resetPassword(data)
            onSuccess()
        } catch (e: HttpException) {
            onError(e.errorText())
        }
    }

    suspend fun update(
        data: UserEmailData,
        onSuccess: suspend () -> Unit = {},
        onError: suspend (message: String) -> Unit = {}
    ) = dispUploader.load {
        try {
            val user = api.updateEmail(data)
            dao.insert(entity(user))
            onSuccess()
        } catch (e: HttpException) {
            onError(e.errorText())
        }
    }

    suspend fun update(
        data: UpdatePasswordData,
        onSuccess: suspend () -> Unit = {},
        onError: suspend (message: String) -> Unit = {}
    ) = dispUploader.load {
        try {
            api.updatePassword(data)
            onSuccess()
        } catch (e: HttpException) {
            onError(e.errorText())
        }
    }

    suspend fun logout() {
        dao.delete()
        dataStorage.setAuthToken("")
    }

    fun get(): Flow<User?> = dao.get()
        .flowOn(Dispatchers.IO).conflate()
        .map { u -> u?.toModel() }

    suspend fun fetchMe() = dispUploader.load {
        val user = api.me()
        dao.delete()
        dao.insert(entity(user))
    }
}