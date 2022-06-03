package ru.boringowl.myroadmapapp.data.room.repos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.boringowl.myroadmapapp.data.datastore.DataStorage
import ru.boringowl.myroadmapapp.data.network.api.UserApi
import ru.boringowl.myroadmapapp.data.network.dto.*
import ru.boringowl.myroadmapapp.data.room.AppDatabase
import ru.boringowl.myroadmapapp.data.room.dao.UserDao
import ru.boringowl.myroadmapapp.data.room.model.UserEntity
import ru.boringowl.myroadmapapp.model.User
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val dao: UserDao,
    private val api: UserApi,
    private val dataStorage: DataStorage,
    private val db: AppDatabase,
) {
    private fun entity(model: User) = UserEntity(model)

    suspend fun register(
        data: RegisterData,
        onSuccess: () -> Unit = {},
        onError: (message: String) -> Unit = {}
    ) = loadWithIO(
        onNetworkError = onError,
        onSuccess = onSuccess,
    ) {
        val token = api.register(data)
        dataStorage.setAuthToken(token.accessToken)
        fetchMe()
    }

    suspend fun login(
        data: LoginData,
        onSuccess: suspend () -> Unit = {},
        onError: suspend (message: String) -> Unit = {}
    ) = loadWithIO(
        onNetworkError = onError,
        onSuccess = onSuccess,
    ) {
        val token = api.auth(data)
        dataStorage.setAuthToken(token.accessToken)
        fetchMe()
    }

    suspend fun resetPassword(
        data: RestorePasswordData,
        onSuccess: suspend () -> Unit = {},
        onError: suspend (message: String) -> Unit = {}
    ) = loadWithIO(
        onNetworkError = onError,
        onSuccess = onSuccess,
    ) { api.resetPassword(data) }

    suspend fun update(
        data: UserEmailData,
        onSuccess: suspend () -> Unit = {},
        onError: suspend (message: String) -> Unit = {}
    ) = loadWithIO(
        onNetworkError = onError,
        onSuccess = onSuccess,
    ) { dao.insert(entity(api.updateEmail(data))) }

    suspend fun update(
        data: UpdatePasswordData,
        onSuccess: suspend () -> Unit = {},
        onError: suspend (message: String) -> Unit = {}
    ) = loadWithIO(
        onNetworkError = onError,
        onSuccess = onSuccess,
    ) { api.updatePassword(data) }

    suspend fun logout() {
        dao.delete()
        dataStorage.setAuthToken("")
        db.flushDB()
    }

    fun get(): Flow<User?> = dao.get()
        .flowOn(Dispatchers.IO).conflate()
        .map { u -> u?.toModel() }

    suspend fun fetchMe() = loadWithIO {
        val user = api.me()
        val localUser = dao.getUser()?.toModel()
        if (user != localUser) {
            dao.delete()
            dao.insert(entity(user))
        }
    }
}