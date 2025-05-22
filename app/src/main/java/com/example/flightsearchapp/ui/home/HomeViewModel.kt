package com.example.flightsearchapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearchapp.data.model.Airport
import com.example.flightsearchapp.data.model.Favorite
import com.example.flightsearchapp.data.database.FlightSearchRepository
import com.example.flightsearchapp.data.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val flightSearchRepository: FlightSearchRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private var _uiState = MutableStateFlow(HomeUiState())
    var uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun setInit(initState: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isInit = initState
            )
        }
    }

    fun setSearchString(searchString: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchText = searchString
            )
        }
        if (uiState.value.isInit == false) {
            viewModelScope.launch {
                savePreferences(searchString)
            }
        }
    }

    suspend fun savePreferences(searchString: String) =
        preferencesRepository.saveSearchString(searchString)

    fun getPreferences(): Flow<String> = preferencesRepository.searchString

    fun getFlightsSearch(searchString: String): Flow<List<Airport>> =
        flightSearchRepository.getFlightsSearchStream(searchString)
        /* if (searchString == "") {
             listOf<List<Airport>>(emptyList()).asFlow()
         } else*/

    fun getFavorites(): Flow<List<Favorite>> = flightSearchRepository.getFavorites()

    fun getFlightsFromAirport(id: String): Flow<List<Favorite>> =
        flightSearchRepository.getFlightsFromAirport(id)

    suspend fun addFavorite(favorite: Favorite) {
        flightSearchRepository.insertFavorite(
            Favorite(
                id = 0,
                departureCode = favorite.departureCode,
                destinationCode = favorite.destinationCode
            )
        )
    }

    fun setCurrentAirport(airport: Airport) {
        _uiState.update { currentState ->
            currentState.copy(
                currentAirports = airport
            )
        }
    }

    fun getCurrentAirport(): Airport? {
        return uiState.value.currentAirports
    }

    fun getAirportNameByIata(iata: String): Flow<String> =
        flightSearchRepository.getAirportNameByIata(iata).filterNotNull()
}

data class HomeUiState(
    var searchText: String = "",
    val currentAirports: Airport? = null,
    var isInit: Boolean = true,
)
