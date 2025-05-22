package com.example.flightsearchapp.data.database

import com.example.flightsearchapp.data.model.Airport
import com.example.flightsearchapp.data.model.Favorite
import kotlinx.coroutines.flow.Flow

class OfflineRepository(
    private val flightSearchDAO: FlightSearchDAO
) : FlightSearchRepository {
    override fun getFlightsSearchStream(searchString: String): Flow<List<Airport>> = flightSearchDAO.getFlightsSearch(searchString)
    override fun getFlightsFromAirport(name: String): Flow<List<Favorite>> = flightSearchDAO.getFlightsFromAirport(name)
    override fun getAirportNameByIata(iata: String): Flow<String?> =
        flightSearchDAO.getAirportNameByIata(iata)

    override fun getFavorites(): Flow<List<Favorite>> = flightSearchDAO.getFavorites()
    override suspend fun insertFavorite(favorite: Favorite) = flightSearchDAO.insertFavorite(favorite)
    override suspend fun deleteFavorite(favorite: Favorite) = flightSearchDAO.deleteFavorite(favorite)
}