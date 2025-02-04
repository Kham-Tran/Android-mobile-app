package com.example.stockanalyser204.Pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.stockanalyser201.LocalDatabase.paperTradeSymbol
import com.example.stockanalyser201.Model.ProductionModel
import com.example.stockanalyser201.Pages.roundDouble

@Composable
fun paperTrade(model: ProductionModel){
    var surfaceColor = MaterialTheme.colorScheme.surface
    var onSurfaceColor = MaterialTheme.colorScheme.onSurface
    var outlineColor = MaterialTheme.colorScheme.outline
    var primaryContainerColor = MaterialTheme.colorScheme.primaryContainer
    var onprimaryContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
    var secondContainerColor = MaterialTheme.colorScheme.secondaryContainer
    var onsecondContainerColor = MaterialTheme.colorScheme.onSecondaryContainer


    val paperTrade by model.paperTrade.collectAsState()
    var (a,b,c) = model.paperTradeValue(paperTrade)

    var totalValue by remember {
        mutableStateOf(a)
    }
    var budgetValue by remember {
        mutableStateOf(b)
    }
    var totalGainLost by remember {
        mutableStateOf(c)
    }
    var tradeBox by remember {
        mutableStateOf(false)
    }
    var chooseBuyorSell by remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier
                .padding(3.dp)
                .weight(.1f)
                .fillMaxWidth()
                .border(3.dp, color = outlineColor)){
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .weight(.2f)
                    .padding(3.dp)
                    .border(1.dp, color = outlineColor)) {
                    Text(text = "$ ${roundDouble(totalValue)}", modifier = Modifier
                        .padding(3.dp)
                        .align(Alignment.Center))
                }
                Column(verticalArrangement = Arrangement.SpaceEvenly,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                    .fillMaxHeight()
                    .weight(.3f)
                    .padding(3.dp)
                    .border(1.dp, color = outlineColor))  {
                    Text(text = "$ ${roundDouble(budgetValue)}",modifier = Modifier.padding(1.dp))
                    Text(text = "$ ${roundDouble(totalGainLost)}",modifier = Modifier.padding(1.dp))
                }

            }
            Column(modifier = Modifier
                .padding(3.dp)
                .weight(.9f)
                .border(3.dp, color = outlineColor)) {
                Box(modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()) {
                    Text(text = "Holding List", modifier = Modifier
                        .padding(1.dp)
                        .align(Alignment.CenterStart))
                    Row(modifier = Modifier
                        .padding(3.dp)
                        .align(Alignment.CenterEnd)) {
                        Box(modifier = Modifier
                            .padding(.5.dp)
                            .border(1.dp, color = outlineColor)
                        ) {
                            var color: Color = if(chooseBuyorSell=="BUY") primaryContainerColor else surfaceColor
                            Text(text = "BUY",modifier = Modifier
                                .padding(.5.dp)
                                .clickable {
                                    tradeBox = !tradeBox
                                    chooseBuyorSell = "BUY"
                                }
                                .background(color = color)

                            )
                        }

                        Spacer(modifier = Modifier.width(2.dp))

                        Box(modifier = Modifier
                            .padding(.5.dp)
                            .border(1.dp, color = outlineColor)) {
                            var color: Color = if(chooseBuyorSell=="SELL") primaryContainerColor else surfaceColor
                            Text(text = "SELL",modifier = Modifier
                                .padding(.5.dp)
                                .clickable {
                                    tradeBox = !tradeBox
                                    chooseBuyorSell = "SELL"
                                }
                                .background(color = color)
                            )
                        }

                    }
                }
                AnimatedContent(targetState = tradeBox, label = "Buy or Sell Box") {
                        targetState ->
                    if (targetState){
                        paperTradeBox(model, tradeBox,onchange = {tradeBox = it}, paperTrade, chooseBuyorSell)
                    }
                    else{

                        LazyColumn(modifier = Modifier.fillMaxWidth()){
                            item {
                                LazyRow() {
                                    item {
                                        Box(modifier = Modifier
                                            .padding(2.dp)
                                            .width(100.dp)
                                            .border(1.dp, color = outlineColor)) {
                                            Text(text = "Symbol", textAlign = TextAlign.Left, modifier = Modifier
                                                .padding(2.dp)
                                            )
                                        }
                                        Box(modifier = Modifier
                                            .padding(2.dp)
                                            .border(1.dp, color = outlineColor)) {
                                            Text(text = "Shares", textAlign = TextAlign.Left, modifier = Modifier
                                                .padding(2.dp)
                                            )
                                        }
                                        Box(modifier = Modifier
                                            .padding(2.dp)
                                            .border(1.dp, color = outlineColor)) {
                                            Text(text = "Price", textAlign = TextAlign.Left, modifier = Modifier
                                                .padding(2.dp)
                                            )
                                        }
                                        Box(modifier = Modifier
                                            .padding(2.dp)
                                            .border(1.dp, color = outlineColor)) {
                                            Text(text = "Market", textAlign = TextAlign.Left, modifier = Modifier
                                                .padding(2.dp)
                                            )
                                        }
                                        Box(modifier = Modifier
                                            .padding(2.dp)
                                            .border(1.dp, color = outlineColor)) {
                                            Text(text = "Ori", textAlign = TextAlign.Left, modifier = Modifier
                                                .padding(2.dp)
                                            )
                                        }
                                        Box(modifier = Modifier
                                            .padding(2.dp)
                                            .border(1.dp, color = outlineColor)) {
                                            Text(text = "Acc", textAlign = TextAlign.Left, modifier = Modifier
                                                .padding(2.dp)
                                            )
                                        }
                                    }
                                }

                            }
                            items(paperTrade){
                                    item ->
                                paperTradeItems(item)
                            }
                        }
                    }
                }

            }

        }
        IconButton(onClick = {
            model.refreshPaperTrading()
            var (a,b,c) = model.paperTradeValue(paperTrade)
            totalValue = a
            budgetValue = b
            totalGainLost = c
        }, modifier = Modifier.align(Alignment.BottomEnd)) {
            Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
        }
    }

}

@Composable
fun paperTradeItems(item: paperTradeSymbol){
    LazyRow( horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth()) {
        item{
            Box(modifier = Modifier
                .padding(2.dp)
                .width(80.dp)
                ) {
                Text(text = item.SName, textAlign = TextAlign.Left)
            }
            Box(modifier = Modifier
                .padding(2.dp)
                .width(80.dp)
            ) {
                Text(text = "$ ${roundDouble(item.holdingShares) }", textAlign = TextAlign.Left)
            }
            Box(modifier = Modifier
                .padding(2.dp)
                .width(80.dp)
            ) {
                Text(text = "$ ${roundDouble(item.holdingPrice) }", textAlign = TextAlign.Left)
            }
            Box(modifier = Modifier
                .padding(2.dp)
                .width(80.dp)
            ) {
                Text(text = "$ ${roundDouble( item.marketPrice)}", textAlign = TextAlign.Left)
            }
            Box(modifier = Modifier
                .padding(2.dp)
                .width(80.dp)
            ) {
                Text(text = "$ ${roundDouble(item.holdingPrice * item.holdingShares)}", textAlign = TextAlign.Left)
            }
            Box(modifier = Modifier
                .padding(2.dp)
                .width(80.dp)
            ) {
                Text(text = "$ ${roundDouble(item.marketPrice * item.holdingShares)}", textAlign = TextAlign.Left)
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun paperTradeBox(
    model: ProductionModel,
    buyorsell: Boolean,
    onchange: (Boolean) -> Unit,
    porfolio: List<paperTradeSymbol>,
    chooseBuyorSell: String,

    ){
    var surfaceColor = MaterialTheme.colorScheme.surface
    var onSurfaceColor = MaterialTheme.colorScheme.onSurface
    var outlineColor = MaterialTheme.colorScheme.outline
    var primaryContainerColor = MaterialTheme.colorScheme.primaryContainer
    var onprimaryContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
    var secondContainerColor = MaterialTheme.colorScheme.secondaryContainer
    var onsecondContainerColor = MaterialTheme.colorScheme.onSecondaryContainer
    var sharesORdollar by remember {
        mutableStateOf("SHARES")
    }
    var priceBymarket by remember {
        mutableStateOf(false)
    }
    var stockSymbol by remember {
        mutableStateOf("")
    }
    var wantedShares by remember {
        mutableStateOf("")
    }
    var wantedPrice by remember {
        mutableStateOf("")
    }
    var sellAll by remember {
        mutableStateOf(false)
    }

    Column(verticalArrangement = Arrangement.SpaceEvenly,horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)
//        .background(primaryContainerColor)
    ) {
        Column(modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()) {
            TextField(value = stockSymbol, onValueChange = {stockSymbol= it}, placeholder = { Text(text = "Enter ticker")}, modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth())
            LazyColumn(modifier = Modifier
                .fillMaxWidth()){

            }
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly,modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
//            .background(primaryContainerColor)
        ) {
            Box(modifier = Modifier
                .padding(2.dp)
                .border(1.dp, color = outlineColor)
                .clickable {
                    sharesORdollar = "SHARES"
                }
//                .background(secondContainerColor)
            ) {
                var color: Color = if(sharesORdollar=="SHARES") primaryContainerColor else surfaceColor
                Text(text = "SHARES", color = onsecondContainerColor, modifier = Modifier
                    .padding(2.dp)
                    .background(color)
                )
            }
            Box(modifier = Modifier
                .padding(2.dp)
                .border(1.dp, color = outlineColor)
                .clickable {
                    sharesORdollar = "DOLLAR"
                }
//                .background(secondContainerColor)
            ) {
                var color: Color = if(sharesORdollar=="DOLLAR") primaryContainerColor else surfaceColor
                Text(text = "DOLLAR", color = onsecondContainerColor, modifier = Modifier
                    .padding(2.dp)
                    .background(color)
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.SpaceEvenly,modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()) {
            Text(text = sharesORdollar)
            TextField(value = wantedShares, onValueChange = {wantedShares = it}, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
        }

        Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.SpaceEvenly,modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()) {
            Box(modifier = Modifier
                .clickable {
                    priceBymarket = !priceBymarket
                    if (priceBymarket){
                        wantedPrice = 0.toString()
                    }else{
                        wantedPrice =""
                    }
                }

            ){
                var color: Color = if(priceBymarket) primaryContainerColor else surfaceColor
                Text(text = "Market", modifier = Modifier.background(color))
            }
            TextField(
                value = wantedPrice,
                onValueChange = {wantedPrice = it},
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    IconButton(onClick = {
                        sellAll = !sellAll
                        if (sellAll){
                            wantedShares = 0.toString()
                        }else{
                            wantedShares =""
                        }

                    }) {
                        Text(text = "All")
                    }
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.SpaceEvenly,modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()) {
            Button(onClick = {
                model.addpaperTradeItem(
                    SName = stockSymbol.uppercase(),
                    Shares = wantedShares.toDouble(),
                    Price = wantedPrice.toDouble(),
                    buyORsell = chooseBuyorSell.uppercase(),
                    sharesORdollar = sharesORdollar.uppercase(),
                    priceBymarket,
                    sellAll)
                onchange(!buyorsell)
            }) {
                Text(text = "Submit")
            }
            Button(onClick = {
                onchange(!buyorsell)
            }) {
                Text(text = "Cancel")
            }
        }

    }
}