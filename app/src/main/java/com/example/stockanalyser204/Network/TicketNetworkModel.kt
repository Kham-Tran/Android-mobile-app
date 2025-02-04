package com.example.stockanalyser201.Network

import androidx.versionedparcelable.ParcelField
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class box(
    @SerialName("chart")
    var chart: Chart
)

@Serializable
data class Chart(
    @SerialName("result")
    var result:List<Result>,
    var error:String?
)

@Serializable
data class Result(
    @SerialName("timestamp")
    var timestamp:List<Long>,

    @SerialName("indicators")
    var indicators: Indicators

)

@Serializable
data class Indicators(
    var quote:List<Quote>,
//    var adjclose:List<Adjclose>
)

@Serializable
data class Adjclose(
    var adjclose:List<Double>
)

@Serializable
data class Quote(
    var low:List<Double?>,
    var close:List<Double?>,
    var high:List<Double?>,
    var open:List<Double?>,
    var volume:List<Long?>

)

@VersionedParcelize
data class Ticket(
    var SName:String = "",
    var open: List<Double> = emptyList(),
    var close: List<Double> = emptyList(),
    var high: List<Double> = emptyList(),
    var low: List<Double> = emptyList(),
    var volume: List<Long> = emptyList(),
    var date:List<Long?> = emptyList(),
    var error: String? = ""
    )