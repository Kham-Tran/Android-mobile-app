package com.example.stockanalyser204.Pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.stockanalyser201.Model.ProductionModel
import com.example.stockanalyser201.Model.stock_info
import org.apache.commons.collections.list.LazyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun pumpDetector(model: ProductionModel) {
    var stocklist = model.stockInfo
    var value by remember {
        mutableStateOf("")
    }
    var filteredList = mutableListOf<stock_info>()
    if(value.isNotEmpty()){
        filteredList = model.stockInfo.filter { it.sector?.uppercase()!!.startsWith(value)   }
            .toMutableList()

    }
//    val pumpList by model.pumpData.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(0.1f)) {
            TextField(value = value, onValueChange = {value = it.uppercase()}, placeholder = { Text(text = "Enter Sector")},modifier = Modifier.weight(.8f))
            Text("Submit",modifier = Modifier.weight(.2f).clickable { model.pumpDetec(value) })
        }
//        LazyColumn(modifier = Modifier
//            .fillMaxWidth()
//            .weight(.8f)){
//            items(pumpList.sortedByDescending { it.volume.last() }.take(10)){
//                item ->
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    item.SName?.let { Text(text = it,modifier = Modifier.weight(.5f)) }
//                    Text(text = "${item.volume.last()}",modifier = Modifier.weight(.5f))
//                }
//            }
//        }
    }
}
