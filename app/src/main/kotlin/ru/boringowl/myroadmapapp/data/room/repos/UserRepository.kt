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
import ru.boringowl.myroadmapapp.model.LoginData
import ru.boringowl.myroadmapapp.model.RegisterData
import ru.boringowl.myroadmapapp.model.RestorePasswordData
import ru.boringowl.myroadmapapp.model.User
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
            Log.e("REG", "TOKEN")
            val token = api.register(data)
            Log.e("REG", token.accessToken)
            dataStorage.setAuthToken(token.accessToken)
            Log.e("REG", "TOKEN SET")
            fetchMe()
            Log.e("REG", "FETCHED")
            onSuccess()
        } catch (e: HttpException) {
            Log.e("REG", e.toString())
            onError(e.errorText())
        }
    }

    suspend fun login(
        data: LoginData,
        onSuccess: () -> Unit = {},
        onError: (message: String) -> Unit = {}
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
        onSuccess: () -> Unit = {},
        onError: (message: String) -> Unit = {}
    ) = dispUploader.load {
        try {
            api.resetPassword(data)
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
        Log.e("REG", "GOT ME")
        dao.delete()
        dao.insert(entity(user))
    }
}