package com.example.flightsearchapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearchapp.data.database.FlightSearchDatabase
import com.example.flightsearchapp.data.database.FlightSearchRepository
import com.example.flightsearchapp.data.database.OfflineRepository
import com.example.flightsearchapp.data.preferences.PreferencesRepository

interface AppContainer {
    val flightSearchRepository: FlightSearchRepository
    val preferencesRepository: PreferencesRepository
}

private const val USER_PREFERENCES = "user_preferences"
private val Context.dateStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES
)

class AppDataContainer(private val context: Context) : AppContainer {
    override val flightSearchRepository: FlightSearchRepository by lazy {
        OfflineRepository(FlightSearchDatabase.getDatabase(context).flightSearchDAO())
    }
    override val preferencesRepository: PreferencesRepository by lazy {
        PreferencesRepository(context.dateStore)
    }
}
