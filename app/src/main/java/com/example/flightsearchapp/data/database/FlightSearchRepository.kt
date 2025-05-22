package com.example.flightsearchapp.data.database

import com.example.flightsearchapp.data.model.Airport
import com.example.flightsearchapp.data.model.Favorite
import kotlinx.coroutines.flow.Flow

interface FlightSearchRepository {
    fun getFlightsSearchStream(searchString: String): Flow<List<Airport>>
    fun getFlightsFromAirport(name: String): Flow<List<Favorite>>
    fun getAirportNameByIata(iata: String): Flow<String?>
    fun getFavorites(): Flow<List<Favorite>>

    suspend fun insertFavorite(favorite: Favorite)
    suspend fun deleteFavorite(favorite: Favorite)
}