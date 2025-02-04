package com.example.stockanalyser201.Navigator

import HomePage
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stockanalyser201.Model.ProductionModel
import com.example.stockanalyser201.Pages.ListPage
import com.example.stockanalyser201.Pages.TicketPage
import com.example.stockanalyser204.Pages.paperTrade
import com.example.stockanalyser204.Pages.porfolioPage
import com.example.stockanalyser204.Pages.pumpDetector

@Composable
fun MyNavigator(model: ProductionModel, Nav: NavController, context: Context){
    var selectedTicket by rememberSaveable {
        mutableStateOf("SPY")
    }
    var selected by rememberSaveable { mutableStateOf("1D") }
    when (selected) {
        "1D" -> model.getGraphData(selectedTicket, "1d", "5m")
        "1W" -> model.getGraphData(selectedTicket, "5d", "5m")
        "2W" -> model.getGraphData(selectedTicket, "10d", "15m")
        "1M" -> model.getGraphData(selectedTicket, "1mo", "30m")
        "3M" -> model.getGraphData(selectedTicket, "3mo", "1d")
        "6M" -> model.getGraphData(selectedTicket, "6mo", "1d")
        "1Y" -> model.getGraphData(selectedTicket, "12mo", "1d")
        "3Y" -> model.getGraphData(selectedTicket, "3y", "1d")
        "5Y" -> model.getGraphData(selectedTicket, "5y", "1d")
        "ytd" -> model.getGraphData(selectedTicket, "ytd", "1d")
        "MAX" -> model.getGraphData(selectedTicket, "max", "1wk")
        else -> {
            model.getGraphData(selectedTicket, "5d", "15m")
        }
    }
    NavHost(navController = Nav as NavHostController, startDestination = Home.route){
        composable(Home.route){
            HomePage(Nav, context )
        }
        composable(Ticket.route){
            TicketPage(nav = Nav,context,selectedTicket, model, onselected = {selected = it},selected)
        }
        composable(List.route){
            ListPage(nav = Nav, context = context ,model, onSelectedChange = {selectedTicket = it})
        }
        
        composable(Porfolio.route){
            porfolioPage(model = model)
        }

        composable(Detector.route){
            pumpDetector(model = model)
        }
        composable(paperTrading.route){
            paperTrade(model = model)
        }
    }
}