package com.example.stockanalyser201.Pages

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.RowScopeInstance.align
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stockanalyser201.LocalDatabase.GroupList
import com.example.stockanalyser201.LocalDatabase.GroupOfSymbol
import com.example.stockanalyser201.LocalDatabase.Symbol
import com.example.stockanalyser201.Model.ProductionModel
import com.example.stockanalyser201.Model.stock_info
import com.example.stockanalyser201.Navigator.Home
import com.example.stockanalyser201.Network.Ticket
import java.math.RoundingMode
import java.text.DecimalFormat

fun refreshList(model:ProductionModel,groupList:List<GroupOfSymbol>){
    model.getInitPriceList()
    model.getListGroup()
}

@Composable
fun ListPage(
    nav: NavController,
    context: Context,
    model: ProductionModel,
    onSelectedChange:(String) -> Unit
    ){
    val listGroup by model.TicketStateFlow.collectAsState()
    var expanded by remember {
        mutableStateOf(false)
    }
    var volBox by remember {
        mutableStateOf(false)
    }
    val Groups by model.listgroup.collectAsState()
    var containerColor = MaterialTheme.colorScheme.primaryContainer
    var contentColor = MaterialTheme.colorScheme.onPrimaryContainer


    Surface(color = MaterialTheme.colorScheme.surface,modifier = Modifier) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)) {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .weight(.9f)
                ){
                    items(listGroup){
                            item ->
                        GroupsComponent(model,item,nav, onSelectedChange)
                    }
                }

                // Search and refresh button
                AnimatedContent(targetState = expanded, label = "",modifier = Modifier
                    .padding(5.dp)
//                    .weight(.1f)
                ) { targetState ->
                    if (targetState) {
                         searchBox(model, expanded = expanded, onExpanded = { expanded = it },Groups)
                    } else {
                        Box(modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()){
                            FloatingActionButton(onClick = {
                                refreshList(model,listGroup)
                            }, modifier = Modifier
                                .padding(5.dp)
                                .align(Alignment.BottomCenter)
                            ) {
                                Icon(Icons.Filled.Refresh, contentDescription = "Refresh",tint = contentColor )
                            }
                            FloatingActionButton(onClick = { expanded = !expanded}, containerColor = containerColor, contentColor = contentColor,modifier = Modifier
                                .padding(5.dp)
                                .align(Alignment.BottomEnd)
                            ){
                                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search button", tint = contentColor)
                            }

                            AnimatedContent(targetState = volBox, label = "volBox") {
                                targetState ->
                                if(targetState){
                                    com.example.stockanalyser201.Pages.volBox(
                                        model = model,
                                        onvolBoxChange = {volBox = it},
                                        volBox = volBox,
                                        nav
                                    )
                                }
                                else{
                                    FloatingActionButton(onClick = {
                                            volBox = !volBox
                                    }, containerColor = containerColor, contentColor = contentColor,modifier = Modifier
                                        .padding(5.dp)
                                        .align(Alignment.BottomStart)
                                    ){
                                        Icon(imageVector = Icons.Filled.Send, contentDescription = "analysis", tint = contentColor)
                                    }
                                }
                            }

                        }
                    }
                }

            }
    }
}


@Composable
fun volBox(model: ProductionModel, onvolBoxChange:(Boolean) -> Unit, volBox:Boolean,nav: NavController){
    val ticker by model.tickerData.collectAsState()
    val vol by model.volumeData.collectAsState()
    val date by model.timeData.collectAsState()
    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(ticker){ item ->
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        nav.navigate(com.example.stockanalyser201.Navigator.Ticket.route)
                    })) {
                    Text(text = item)
                    Text(text = volumeToString(vol[ticker.indexOf(item)]))
                    Text(text = timeStampToDate(date[ticker.indexOf(item)]))
                }
            }
        }
        IconButton(onClick = { onvolBoxChange(!volBox) }, modifier = Modifier.align(Alignment.TopEnd)) {
        Icon(Icons.Filled.Close, contentDescription = "Close")
    }
    }
}


@Composable
fun GroupsComponent(model: ProductionModel,item: GroupOfSymbol,nav: NavController, onSelectedChange: (String) -> Unit){
    var containerColor = MaterialTheme.colorScheme.primaryContainer
    var contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    var selectedSymbol by remember {
        mutableStateOf("")
    }
    var style = MaterialTheme.typography.titleMedium
        Column(modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(containerColor)) {

                Box(modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .background(containerColor)){
                    Text(text = item.group.GroupName, color = contentColor, style = style, modifier = Modifier.align(
                        Alignment.Center))
                    IconButton(onClick = {model.deleteGroup(item.group.GroupName)
                                         model.getListGroup()}, modifier = Modifier.align(
                        Alignment.CenterEnd)) {
                        Icon(Icons.Filled.Close, contentDescription ="Delete Group" , tint = contentColor)
                    }
                }
            Column(modifier = Modifier.fillMaxWidth()) {
                item.symbolList.forEach(){
                        item ->

                    TicketItem(item = item,selectedSymbol, onSelectedSymbol =  { selectedSymbol = it },nav,onSelectedChange,model)
                }
            }
            }
//    }
//    else {
//        Column(modifier = Modifier
//            .padding(2.dp)
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(10.dp))
//            .background(containerColor)) {
//            Box(modifier = Modifier
//                .padding(5.dp)
//                .fillMaxWidth()
//                .background(containerColor)){
//                Text(text = item.group.GroupName, color = contentColor, style = style, modifier = Modifier.align(
//                    Alignment.Center))
//                IconButton(onClick = {model.deleteGroup(item.group.GroupName)}, modifier = Modifier.align(
//                    Alignment.CenterEnd)) {
//                    Icon(Icons.Filled.Close, contentDescription ="Delete Group" , tint = contentColor)
//                }
//            }
//            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxWidth()) {
//                d.forEach() { item ->
//                    TicketItem(item = item, selectedSymbol = selectedSymbol, onSelectedSymbol = {selectedSymbol = it}, nav , onSelectedChange,model)
//                }
//            }
//        }
//    }
}

@Composable
fun TicketItem(item: Symbol, selectedSymbol: String, onSelectedSymbol: (String) -> Unit,nav: NavController, onSelectedChange: (String) -> Unit, model: ProductionModel){
    var containerColor = MaterialTheme.colorScheme.secondary
    var contentColor = MaterialTheme.colorScheme.onSecondary
    var style = MaterialTheme.typography.bodyLarge
    var styleDetail = MaterialTheme.typography.bodyMedium

    // unexpand item info
    Box(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()
        .clickable {
            nav.navigate(com.example.stockanalyser201.Navigator.Ticket.route)
            onSelectedChange(item.SName)
        }
        .background(containerColor)) {
        Text(text = item.SName, color = contentColor,style=style, modifier = Modifier
            .padding(5.dp)
            .align(
                Alignment.CenterStart
            ))
        Text(text = "$ ${item.close?.let { roundOffDecimal(it) }}",color = contentColor, style = style,modifier = Modifier
            .padding(5.dp)
            .align(
                Alignment.Center
            ))
        IconButton(onClick = {onSelectedSymbol(item.SName)},modifier = Modifier
            .padding(5.dp)
            .align(
                Alignment.CenterEnd
            )) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Expand", tint = contentColor)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchBox(
    model: ProductionModel,
    expanded: Boolean,
    onExpanded: (Boolean) -> Unit,
    Groups:List<GroupList>
) {
        var listMode by remember {
            mutableStateOf("All")
        }

        var list = mutableListOf<stock_info>()
    
        var keyword by remember {
            mutableStateOf("")
        }
        var groupName by remember {
            mutableStateOf("")
        }

        if(keyword.isNotEmpty()){
            if  (listMode == "All"){
            list =
                model.stockInfo.filter { it.symbol!!.startsWith(keyword) or it.name?.uppercase()!!.contains(keyword)  }
                    .toMutableList()
            }else if(listMode == "Sector"){
                var sector = model.stockInfo.filter { it.symbol.equals(keyword) }
                if(!sector.isEmpty()){list = model.stockInfo.filter {it.sector!!.equals(sector[0].sector) }
                    .toMutableList()}

            }
        }
    var containerColor = MaterialTheme.colorScheme.primaryContainer
    var secondcontainerColor = MaterialTheme.colorScheme.secondaryContainer
    var contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    var secondcontentColor = MaterialTheme.colorScheme.onSecondaryContainer
    var style = MaterialTheme.typography.bodyLarge
    Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier
        .fillMaxWidth()
        .background(containerColor)) {

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .heightIn(50.dp, 300.dp)
//                .align(Alignment.TopCenter)
            .background(Color.Transparent)){
            items(list){item->
                Row(horizontalArrangement = Arrangement.SpaceEvenly,modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .clickable {
                        keyword = item.symbol.toString()
                    }) {
                    item.symbol?.let { Text(text = it ,maxLines = 1, textAlign = TextAlign.Left, style = style, modifier = Modifier
                        .padding(2.dp)
                        .weight(.2f))}
                    item.name?.let { Text(text = it ,maxLines = 1, textAlign = TextAlign.Left, style = style,modifier = Modifier
                        .padding(2.dp)
                        .weight(.6f))}
                    item.sector?.let { Text(text = it ,maxLines = 1, textAlign = TextAlign.Left, style = style,modifier = Modifier
                        .padding(2.dp)
                        .weight(.3f)) }
                }
            }
        }
        Box(modifier = Modifier
//                .align(Alignment.BottomCenter)
        ){
            IconButton(onClick = {onExpanded(!expanded)}, modifier = Modifier
                .padding(5.dp)
                .size(50.dp, 50.dp)
                .align(
                    Alignment.TopEnd
                )) {
                Icon(Icons.Filled.Close, contentDescription = "Close" , tint = Color.Black)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)) {
                Spacer(modifier = Modifier.height(50.dp))
                LazyRow(horizontalArrangement = Arrangement.SpaceEvenly,modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()){
                    item {
                        var color:Color = if (listMode=="All") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        IconButton(onClick = {listMode = "All"},modifier = Modifier
                            .wrapContentSize()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                color
                            )) {
                            Text(text = "All",maxLines=1, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                    item {
                        var color:Color = if (listMode=="Sector") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        IconButton(onClick = {listMode="Sector"}, modifier = Modifier
                            .wrapContentSize(
                                Alignment.Center
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                color
                            )) {
                            Text(text = "Sector",maxLines=1,color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }

                //Group Name Input Textfield
                Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()) {
                    var color:Color
                    TextField(value = groupName, onValueChange = {groupName = it} , placeholder = {
                        Text(text ="Enter Group Name",maxLines=1)},
                        trailingIcon = { IconButton(onClick = {groupName =""
                        color = Color.Gray
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Close" )
                    }})

                    LazyRow(horizontalArrangement = Arrangement.SpaceEvenly,modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()){
                        items(Groups){
                                item ->
                            color = if (item.GroupName == groupName) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                            IconButton(onClick = { groupName = item.GroupName}, modifier = Modifier
                                .wrapContentSize()
                                .clip(RoundedCornerShape(20.dp))
                                .background(color)) {
                                Text(text = item.GroupName,maxLines=1,color = MaterialTheme.colorScheme.onPrimary)
                            }

                        }
                    }
                }
                Row {
                    TextField(value = keyword,
                        onValueChange ={keyword =it.uppercase()},
                        placeholder = { Text(text = "Enter Symbol",maxLines=1)},
                        trailingIcon = { IconButton(onClick = { keyword="" }) {
                            Icon(Icons.Filled.Close, contentDescription = "Close" )
                        }},
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(.7f))

                    Button(onClick = {
                        model.addSymbol(
                            keyword.uppercase()
                            ,groupName.uppercase())
                        onExpanded(!expanded)
                        // Update group
                        model.getListGroup()
                                     },
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(.3f)) {
                        Text(text = "Submit",maxLines=1)
                    }
                }
            }

        }

    }
    }


fun roundOffDecimal(number: Double): Double? {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toDouble()
}

//fun volumeToString(volume:Long):String{
//    var v = volume.toFloat()/1000000F
//    return "${v} M"
//}

