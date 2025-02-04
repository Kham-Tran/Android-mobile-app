package com.example.stockanalyser201.Pages

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stockanalyser201.Model.ProductionModel
import java.text.SimpleDateFormat
import java.util.Locale

fun roundDouble(num:Double): Double{

    val number3digits:Double = Math.round(num * 1000.0) / 1000.0
    val number2digits:Double = Math.round(number3digits * 100.0) / 100.0
    val solution:Double = Math.round(number2digits * 10.0) / 10.0
    return number3digits
}

private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.ENGLISH)
fun timeStampToDate(timestamp: Long):String{
    return simpleDateFormat.format(timestamp*1000L)
}



@Composable
fun Float.toDp() = with(LocalDensity.current) {this@toDp.toDp()}

var TicketSaver = listSaver<com.example.stockanalyser201.Network.Ticket,Any>(
    save = { listOf(it.SName,it.open ,it.close,it.high ,it.low ,it.volume ,it.date ,it.error as String) },
    restore = {com.example.stockanalyser201.Network.Ticket(it[0] as String, it[1] as List<Double>,
        it[2] as List<Double>, it[3] as List<Double>, it[4] as List<Double>, it[5] as List<Long>, it[6] as List<Long>, it[7] as String)}
)

@Composable
fun TicketPage(
    nav: NavController,
    context: Context,
    selectedTicket: String,
    model: ProductionModel,
    onselected: (String) -> Unit,
    Selected: String
){
    var listInterval = listOf<String>("1D","1W","2W","1M","3M","6M","1Y","3Y","5Y","ytd","MAX")
    var selected by rememberSaveable { mutableStateOf(Selected) }
    var date by rememberSaveable { mutableStateOf(emptyList<Long>()) }
    var price by rememberSaveable { mutableStateOf(emptyList<Double>()) }
    var volume by rememberSaveable { mutableStateOf(emptyList<Long>())}
    var error by rememberSaveable { mutableStateOf("")}
    val data by model.grapData.collectAsState()
        date = data.date as List<Long>
        price = data.close
        volume = data.volume

    var configuration = LocalConfiguration.current
    Surface(modifier = Modifier.fillMaxSize()) {
        when (configuration.orientation){
            Configuration.ORIENTATION_LANDSCAPE -> {
                fullScreenGraph(listInterval,selected,onChangeSelected = {selected=it}, date,price,error, onselected, volume,selectedTicket)
            }
            else -> {
                miniGraph(listInterval,selected,onChangeSelected = {selected=it}, date,price,error, onselected, volume,selectedTicket)
            }
        }
    }
}


@Composable
fun fullScreenGraph(
    listInterval: List<String>,
    selected: String,
    onChangeSelected: (String) -> Unit,
    date: List<Long>,
    price: List<Double>,
    error: String,
    onselected: (String) -> Unit,
    volume: List<Long>,
    selectedTicket: String,

    ) {
    var expandInterval by remember {
     mutableStateOf(false)
    }
    var width by rememberSaveable {
        mutableStateOf(0f)
    }
    var left by rememberSaveable {
        mutableStateOf(150f)
    }
    var right by rememberSaveable {
        mutableStateOf(350f)
    }
    var indexLeft by remember {
        mutableStateOf(0)
    }
    var indexRight by remember {
        mutableStateOf(0)
    }

    var percent by remember {
        mutableStateOf(0.0)
    }



    Box(modifier = Modifier.fillMaxSize()){

        Canvas(modifier = Modifier.fillMaxSize()){
            width = size.width
        }
        Box(modifier = Modifier.fillMaxSize()){
            Box(modifier = Modifier
                .fillMaxHeight()
                .width(60.dp)
                .drawBehind {
                    if (left < width - 30f && width > 30f) {
                        drawLine(
                            Color.Blue,
                            Offset(left, 0f),
                            Offset(left, size.height),
                            strokeWidth = 4f
                        )
                    }

                }
                .offset(left.toDp() - 20.dp, 0.dp)
                .clip(RoundedCornerShape(50.dp))
                .pointerInput(key1 = null,) {
                    detectHorizontalDragGestures(onDragStart = { offset -> },
                        onDragEnd = { },
                        onHorizontalDrag = { _, dragAmount -> left += dragAmount })
                }
                .background(Color.Transparent)
//                        .align(Alignment.TopStart)
            )

            Box(modifier = Modifier
                .fillMaxHeight()
                .width(60.dp)
                .drawBehind {
                    if (right < width - 30f && right > 30f) {
                        drawLine(
                            Color.Black,
                            Offset(right, 0f),
                            Offset(right, size.height),
                            strokeWidth = 4f
                        )
                    }
                }
                .offset(right.toDp() - 20.dp, 0.dp)
                .clip(RoundedCornerShape(50.dp))
                .pointerInput(key1 = null,) {
                    detectHorizontalDragGestures(onDragStart = { offset -> },
                        onDragEnd = { },
                        onHorizontalDrag = { _, dragAmount -> right += dragAmount })
                }
                .background(Color.Transparent)
//                        .align(Alignment.TopEnd)
            )
        }


        Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(.9f)) {
                Canvas(modifier = Modifier.fillMaxSize()) {

                    if (!date.isEmpty() && !price.isEmpty()) {
                        var volumehigh = size.height/5
                        var (minYPoint, maxYPoint) = price.findMinMax()
                        var x = roundDouble((size.width / date.size).toDouble())
                        var (minV, maxV) = volume.findMinMaxPair()
                        var y = price.map { item -> (size.height - (((item - minYPoint).toFloat() / (maxYPoint - minYPoint).toFloat()) * size.height)) }
                        var v = volume.map { item ->(size.height - ( (((item - minV).toFloat() / (maxV - minV).toFloat()) * ((volumehigh)))))  }
                        drawCoordination(drawScope = this)
                        drawGraph(drawScope = this, x.toFloat(), y)
                        drawVolume(drawScope = this,x.toFloat(),v)
                        indexLeft = (roundDouble(left.toDouble())/x).toInt()
                        indexRight = (roundDouble(right.toDouble())/x).toInt()
                    }
                }
            }
            Box(modifier = Modifier.weight(.1f)){
                if(date.isNotEmpty()) {
                    if(indexLeft > date.size || indexRight > date.size){
                        left = 150f
                        right = 350f
                    }
                    else{
                        Text(

                            text = volumeToString(volume[indexLeft])+"-----${timeStampToDate(date[indexLeft])} ---- ${timeStampToDate(date[indexRight])}-----"+volumeToString(volume[indexRight]),
                            modifier = Modifier
                            .align(Alignment.BottomCenter)

                        )
                        Text(
                            text = "${selectedTicket} : ${roundDouble(((price[indexRight] - price[indexLeft]) / price[indexLeft])*100)}%",
                            modifier = Modifier
                            .align(Alignment.TopCenter)
                        )
                    }
                }
            }
        }

        AnimatedContent(targetState = expandInterval, label = "") {
            targetState ->
            if (targetState){
                Row(
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterEnd)) {
                    IconButton(onClick = { expandInterval = !expandInterval}) {
                        Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Unexpanded")
                    }
                    LazyColumn(modifier = Modifier
                        .wrapContentSize()) {
                        items(listInterval){ item ->
                            intervalItem(item = item, selected = selected, onSelected = onChangeSelected, onselected = onselected )
                        }
                    }
                }
                
            }
            else{
                IconButton(onClick = { expandInterval = !expandInterval }, modifier = Modifier.align(
                    Alignment.CenterEnd)) {
                    Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Expand interval")
                }
            }
        }
    }
}

@Composable
fun miniGraph(
    listInterval: List<String>,
    selected: String,
    onChangeSelected: (String) -> Unit,
    date: List<Long>,
    price: List<Double>,
    error: String,
    onselected: (String) -> Unit,
    volume: List<Long>,
    selectedTicket: String
) {
    var showPrice by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    var indexPrice by remember { mutableStateOf(0) }
    Column {
        // Mini graph
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
        ){
            Canvas(modifier = Modifier
                .fillMaxSize()
                .pointerInput(key1 = null,) {
                    detectHorizontalDragGestures(onDragStart = { offset ->
                        offsetX = offset.x; showPrice = !showPrice
                    },
                        onDragEnd = { offsetX = 0f; showPrice = !showPrice },
                        onHorizontalDrag = { _, dragAmount -> offsetX += dragAmount })
                }
            ){
                if (!date.isEmpty() && !price.isEmpty()){
                    var volumehigh = size.height/5
                    var (minYPoint, maxYPoint) = price.findMinMax()
                    var (minV, maxV) = volume.findMinMaxPair()
                    var x = roundDouble((size.width / date.size).toDouble())
                    var y = price.map { item -> (size.height - (((item - minYPoint).toFloat() / (maxYPoint - minYPoint).toFloat()) * size.height)) }
                    var v = volume.map { item ->(size.height - ( (((item - minV).toFloat() / (maxV - minV).toFloat()) * ((volumehigh)))))  }
                    drawCoordination(drawScope = this)
                    drawGraph(drawScope =  this,x.toFloat(),y)
                    drawVolume(drawScope = this,x.toFloat(),v)
                    indexPrice = (roundDouble(offsetX.toDouble())/x).toInt()
                }

                if(offsetX<size.width-30f && offsetX>30f){
                    drawLine(Color.Black, Offset(offsetX,0f), Offset(offsetX,size.height), strokeWidth = 4f)}
            }
            androidx.compose.animation.AnimatedVisibility(visible = showPrice) {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .offset(
                            x = if (offsetX.toDp() <= 150.dp) offsetX.toDp() else offsetX.toDp() - 100.dp,
                            y = 150.dp
                        )
                        .height(100.dp)
                        .width(100.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (price.isNotEmpty()) {
                            Text(
                                text = "$ ${roundDouble(price[indexPrice])}",
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                            )
                            Text(
                                text = "${timeStampToDate(date[indexPrice])}",
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                            )
                            Text(
                                text = volumeToString(volume[indexPrice]),
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                            )
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier
            .padding(5.dp)
        ){
            LazyRow(){
                items(listInterval){item -> intervalItem(item,selected, onChangeSelected, onselected)}
            }
        }

        //Interval panel
        Box(modifier = Modifier
            .fillMaxWidth()
        ){
            LazyRow(){

            }
        }
    }


}


@Composable
fun intervalItem(item: String, selected: String, onSelected: (String) -> Unit, onselected: (String) -> Unit) {
    var color:Color = if (selected.equals(item)) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.secondaryContainer
    IconButton(onClick = {
        onSelected(item)
        onselected(item)
                         }, modifier = Modifier
        .padding(5.dp)
        .wrapContentSize()
        .clip(
            RoundedCornerShape(50.dp)
        )
        .background(color)) {
        var color:Color = if (selected.equals(item)) MaterialTheme.colorScheme.secondaryContainer  else MaterialTheme.colorScheme.onSecondaryContainer
        Text(text = item, color = color, fontSize = 20.sp, modifier = Modifier
            .padding(5.dp)
        )
    }
}


fun drawGraph(drawScope: DrawScope,x:Float,y:List<Float>){
    var color:Color = if (y[0] > y[y.lastIndex]) Color.Green else Color.Red
    var temp = 0f
    var path = Path()
    path.moveTo(temp, y[0])
    for (y1 in y){
        temp+=x
        path.lineTo(temp,y1)

    }
    println{"price size:${y.size}"}
    drawScope.drawPath(path = path, color ,style = Stroke(width = 10f))
}

fun drawVolume(drawScope: DrawScope,x:Float,y:List<Float>){
    var color = Color.Blue
    var temp = 0f
    var path = Path()
    path.moveTo(temp, y[0])
    for (y1 in y){
        temp+=x
        path.lineTo(temp,y1)

    }
    println{"price size:${y.size}"}
    drawScope.drawPath(path = path, color ,style = Stroke(width = 10f))
}

fun drawCoordination(drawScope:DrawScope,){
    var s = drawScope.size

    for (a  in s.width.toInt() downTo 0 step 70){
        drawScope.drawLine(color = Color.LightGray,start = Offset(a.toFloat(),0f), end = Offset(a.toFloat(),s.height))
    }
    for (a  in s.height.toInt() downTo 0 step 70){
        drawScope.drawLine(color = Color.LightGray,start = Offset(0f, a.toFloat()), end = Offset(s.width,
            a.toFloat()
        )
        )
    }
}


fun List<Double>.findMinMax():Pair<Double,Double>{
    var min = this[0]
    var max = this[0]
    for (item in this){
        if (item.isNotNull()){
            if(item<=min){ min = item}
            else if(item>max){max = item}
        }
    }
    return Pair(min-0.5f,max+.5f)
}

fun List<Long>.findMinMaxPair():Pair<Float,Float>{
    var min = this[0]
    var max = this[0]
    for (item in this){
        if (item!=null){
            if(item<=min){ min = item}
            else if(item>max){max = item}
        }
    }
    return Pair(min.toFloat()- 0.5f,max.toFloat()+.5f)
}

fun Double.isNotNull():Boolean{
    return this!=null
}

fun volumeToString(volume:Long):String{
    var v = volume.toFloat()/1000000F
    return "${v} M"
}