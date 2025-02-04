package com.example.stockanalyser204

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import com.example.stockanalyser204.ui.theme.AppTheme
import com.example.stockanalyser201.Model.ProductionModel
import com.example.stockanalyser201.Navigator.MyNavigator

fun isInternetAvailable(context: Context): Boolean {

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return connectivityManager.isDefaultNetworkActive

}

class MainActivity : ComponentActivity() {
    lateinit var model:ProductionModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                var connectedStatus = MutableLiveData<Boolean>()
                connectedStatus.value = isInternetAvailable(LocalContext.current)
//                if(connectedStatus.value == true) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        model = ProductionModel(LocalContext.current, connectedStatus.value!!)
                        MyNavigation(model)
                    }
//                }
//                else{
//                    Surface(modifier = Modifier.fillMaxSize()) {
//                        model = ProductionModel(LocalContext.current, connectedStatus.value!!)
//                        Text(text = "No connection")
//                    }
//                }
            }
        }
    }
}


@Composable
fun MyNavigation(model:ProductionModel){
    var Nav = rememberNavController()
    MyNavigator(model, Nav = Nav, context = LocalContext.current)
}

