package com.example.stockanalyser201.Network

import android.content.Context
import android.net.ConnectivityManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.headers
//import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun isInternetAvailable(context: Context): Boolean {

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return connectivityManager.isDefaultNetworkActive

}

class NetworkAPI(context: Context) {
    var isavilable = isInternetAvailable(context)
    private val client = HttpClient(Android){
        install(ContentNegotiation){
            json(Json{
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        headers {
            append("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 Edg/129.0.0.0")
//           append("Referer", "https://www.msn.com/")
//           append("authority", "assets.msn.com")
//           append("Sec-fetch-mode", "cors")
//           append("Sec-fetch-site", "same-site")
//           append("Sec-ms-gec", "C6C8B5896101BD6E20A0552692548344A97D3A09E7AE7E73DD4763B1BFB219D2")
        }
    }



    private suspend fun fetch(name:String,interval:String , range:String): HttpResponse {
        return  client.get("https://query1.finance.yahoo.com/v8/finance/chart/${name}?metrics=high?&interval=${range}&range=${interval}")
    }

    suspend fun getTicketsPrice(name: String, interval: String="1m",range: String="1d"):Ticket{
        var t = Ticket()
//        try{
            var NetData: box = fetch(name,interval,range).body()
            var TimeStamp = NetData.chart.result[0].timestamp
            var indicators = NetData.chart.result[0].indicators.quote[0]
            var error = NetData.chart.error
            t =  Ticket(name.uppercase(),indicators.open.filterNotNull(),indicators.close.filterNotNull(),indicators.high.filterNotNull(),indicators.low.filterNotNull(),indicators.volume.filterNotNull(),TimeStamp,"")
//        }catch (e:Exception){
//            println("Error")
//        }
        return  t

    }


}
