package com.example.stockanalyser204.Scrapping

data class EarningData(
    var Symbol:String,
    var Company:String,
    var EarningDate:String?,
    var ESP_Estimate:String?,
    var Reported_ESP:String?,
    var Surprise:String?
)
