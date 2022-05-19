package ru.boringowl.myroadmapapp.data.datastore
import kotlinx.coroutines.flow.Flow

interface DataStorage {

    fun selectedTheme() : Flow<String>
    suspend fun setSelectedTheme(theme: String)

}