package ru.boringowl.myroadmapapp.data.room.repos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import ru.boringowl.myroadmapapp.data.datastore.DataStorage
import ru.boringowl.myroadmapapp.data.network.UserApi
import ru.boringowl.myroadmapapp.model.User
import ru.boringowl.myroadmapapp.data.room.dao.UserDao
import ru.boringowl.myroadmapapp.data.room.model.UserEntity
import ru.boringowl.myroadmapapp.model.LoginData
import ru.boringowl.myroadmapapp.model.RegisterData
import java.util.*
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val dao: UserDao,
    private val api: UserApi,
    private val dataStorage: DataStorage
) {
    private var isLoading by mutableStateOf(false)

    private val dispUploader = DispUploader({ isLoading = true }, { isLoading = false })

    private fun entity(model: User) = UserEntity(model)

    suspend fun register(data: RegisterData) = dispUploader.load {
        val token = api.register(data)
        dataStorage.setAuthToken(token.accessToken)
        val user = api.me()
        delete()
        dao.insert(entity(user))
    }

    suspend fun login(data: LoginData) = dispUploader.load {
        val token = api.auth(data)
        dataStorage.setAuthToken(token.accessToken)
        val user = api.me()
        delete()
        dao.insert(entity(user))
    }

    suspend fun delete() = dao.delete()

    fun get(): Flow<User> = dao.get()
        .flowOn(Dispatchers.IO).conflate()
        .map { u -> u.toModel() }

    suspend fun fetchMe() = dispUploader.load {
        val user = api.me()
        delete()
        dao.insert(entity(user))
    }
}