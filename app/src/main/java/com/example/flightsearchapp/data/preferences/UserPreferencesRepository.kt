package com.example.flightsearchapp.data.preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun saveSearchString(searchString: String)
    val searchString: Flow<String>
}