package com.example.flightsearchapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.ui.AppViewModelProvider
import com.example.flightsearchapp.ui.home.HomeScreen
import com.example.flightsearchapp.ui.home.HomeViewModel

@Composable
fun FlightSearchApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        //topBar = { FlightSearchTopAppBar() }
    ) { innerPadding ->
        HomeScreen(
            modifier = Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchTopAppBar() {
    CenterAlignedTopAppBar(
        title = { Text("Flight Search App") },
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    )
}