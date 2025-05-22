package com.example.flightsearchapp.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.data.model.Airport
import com.example.flightsearchapp.data.model.Favorite
import com.example.flightsearchapp.ui.AppViewModelProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.factory),
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.uiState.collectAsState().value
    val preferences = viewModel.getPreferences().collectAsState("").value.toString()
    val searchString = uiState.searchText
    val initState = uiState.isInit

    Column(
        modifier = modifier
            .fillMaxSize()
    )
    {
        AirportSearchBar(
            setSearchString = viewModel::setSearchString,
            setCurrentAirport = viewModel::setCurrentAirport,
            getFlightsSearch = viewModel::getFlightsSearch,
            searchString = searchString,
            setInit = viewModel::setInit,
            preferences = preferences,
            initState = initState,
            savePreferences = { },
            onSearch = { }
        )

        val scope = rememberCoroutineScope()
        val list: State<List<Favorite>>
        var isFavorite: Boolean = false

        if (uiState.currentAirports?.name?.isNotEmpty() == true &&
            searchString.isEmpty() == false
        ) {
            isFavorite = false
            list =
                viewModel.getFlightsFromAirport(viewModel.getCurrentAirport()?.name.toString())
                    .collectAsState(emptyList())
        } else {
            isFavorite = true
            list = viewModel.getFavorites().collectAsState(emptyList())
        }
        LazyColumn {
            items(
                items = list.value
            ) { item ->
                val airportDeparture =
                    viewModel.getAirportNameByIata(item.departureCode.toString())
                val airportDestination =
                    viewModel.getAirportNameByIata(item.destinationCode.toString())

                ListItem(
                    headlineContent = {
                        AirportItem(
                            iataDeparture = item.departureCode.toString(),
                            iataDestination = item.destinationCode.toString(),
                            airportDepartureName = airportDeparture.collectAsState("").value.toString(),
                            airportDestinationName = airportDestination.collectAsState("").value.toString(),
                            isFavorite = isFavorite
                        )
                    },
                    modifier = Modifier
                        .clickable {
                            if (!isFavorite) {
                                scope.launch {
                                    viewModel.addFavorite(item)
                                }
                            }
                        }
                        .fillMaxWidth()
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportSearchBar(
    setSearchString: (String) -> (Unit),
    setCurrentAirport: (Airport) -> (Unit),
    getFlightsSearch: (String) -> (Flow<List<Airport>>),
    searchString: String,
    setInit: (Boolean) -> (Unit),
    preferences: String,
    initState: Boolean,
    savePreferences: (String) -> (Unit),
    onSearch: (String) -> (Unit),
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var resultList: List<Airport> = emptyList()

    if (initState == true) {
        setSearchString(preferences)
    }
    resultList = getFlightsSearch(searchString).collectAsState(emptyList()).value

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                modifier = Modifier.fillMaxWidth(),
                query = searchString,
                onQueryChange = {
                    setSearchString(it)
                },
                onSearch = {
                    setSearchString(it)
                    Log.d("Michael", "init 2 onSearch " + it)
                },
                expanded = expanded,
                onExpandedChange = {
                    setInit(false)
                    expanded = it
                },
                placeholder = { Text("Enter airport name or iata") },
                leadingIcon = {
                    if (expanded) {
                        IconButton(
                            onClick = {
                                expanded = false
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                        }
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        setInit(false)
                        setSearchString("")
                    }
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = null)
                    }
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items = resultList) { item ->
                ListItem(
                    headlineContent = { Text(item.name.toString()) },
                    modifier = Modifier
                        .clickable {
                            setSearchString(item.name)
                            setCurrentAirport(item)
                            expanded = false
                        }
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun AirportItem(
    iataDeparture: String,
    iataDestination: String,
    airportDepartureName: String,
    airportDestinationName: String,
    isFavorite: Boolean,
) {

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                //.fillMaxWidth()
                .padding(5.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .heightIn(min = 140.dp, max = 200.dp),
                verticalArrangement = Arrangement.SpaceEvenly
                //.background(color = Color.Blue)
            ) {
                Text("Depart")
                Row {
                    Text(
                        text = iataDeparture,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = airportDepartureName,
                        maxLines = 2
                    )
                }
                Text("Arrive")
                Row {
                    Text(
                        text = iataDestination,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = airportDestinationName,
                        maxLines = 2
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 140.dp, max = 200.dp)
                    .widthIn(min = 140.dp),
                //.background(color = Color.Red),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isFavorite)
                        Icons.Default.Star
                    else
                        Icons.Default.StarOutline,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )

            }
        }
    }
}


@Preview
@Composable
fun AirportItemPreview() {
    AirportItem(
        iataDeparture = "MAD",
        iataDestination = "ARN",
        airportDepartureName = "Adolfo Suárez Madrid–Barajas Airport",
        airportDestinationName = "Stockholm Arlanda Airport",
        isFavorite = false
    )
}

@Preview
@Composable
fun AirportItemFavoritePreview() {
    AirportItem(
        iataDeparture = "MAD",
        iataDestination = "ARN",
        airportDepartureName = "Adolfo Suárez Madrid–Barajas Airport",
        airportDestinationName = "Stockholm Arlanda Airport",
        isFavorite = true
    )
}