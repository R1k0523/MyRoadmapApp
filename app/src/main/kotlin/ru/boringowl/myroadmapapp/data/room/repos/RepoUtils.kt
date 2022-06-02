package ru.boringowl.myroadmapapp.data.room.repos

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.boringowl.myroadmapapp.data.network.errorText
import java.net.UnknownHostException

suspend fun loadWithIO(
    onError: suspend (() -> Unit) = {},
    onNetworkError: suspend ((msg: String) -> Unit) = {},
    onSuccess: suspend (() -> Unit) = {},
    onFinish: suspend (() -> Unit) = {},
    toDo: suspend () -> Unit,
) {
    withContext(Dispatchers.IO) {
        try {
            toDo()
            onSuccess()
        } catch (e: HttpException) {
            onNetworkError(e.errorText())
        } catch (e: UnknownHostException) {
            onNetworkError("Отсутствует интернет-соединение")
        } catch (ex: Exception) {
            Log.e("OSHIBKA", ex.message.toString())
            onError()
        } finally {
            onFinish()
        }
    }
}