package ru.boringowl.myroadmapapp.data.room.repos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DispUploader(val onStart: () -> Unit, val onFinish: () -> Unit) {
    suspend fun load(onError: suspend (() -> Unit) = {}, toDo: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            onStart()
            try {
                toDo()
            } catch (ex: Exception) {
                ex.printStackTrace()
                onError()
            } finally {
                onFinish()
            }
        }
    }
}