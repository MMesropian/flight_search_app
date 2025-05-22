package com.example.flightsearchapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearchapp.data.model.Airport
import com.example.flightsearchapp.data.model.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightSearchDAO {

    @Query("SELECT d.id, d.iata_code as departure_code, a.iata_code as destination_code  FROM airport as d  JOIN airport a WHERE a.name = :name")
    fun getFlightsFromAirport(name: String): Flow<List<Favorite>>

    @Query("SELECT * FROM airport WHERE name LIKE ('%' || :searchString || '%') OR iata_code LIKE ('%' || :searchString || '%')")
    fun getFlightsSearch(searchString: String): Flow<List<Airport>>

    @Query("SELECT name FROM airport WHERE iata_code = :iata LIMIT 1")
    fun getAirportNameByIata(iata: String): Flow<String?>

    @Query("SELECT * FROM favorite")
    fun getFavorites(): Flow<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

}