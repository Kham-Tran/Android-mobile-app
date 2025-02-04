package com.example.stockanalyser201.Model

//data class stock_info(
//    var ticker:String? = null,
//    var name:String? = null ,
//    var exchange:String?=null
//)

data class stock_info(
    var symbol:String?,
    var name:String?,
    var lastSale:String?,
    var netChange:String?,
    var percentChange:String?,
    var marketCap:String?,
    var country:String?,
    var IPOyear:String?,
    var volume:String?,
    var sector:String?,
    var industry:String?
){
//    constructor(symbol:String,
//                 name:String?,
//                 lastSale:String?,
//                netChange:String?,
//                 percentChange:String?,
//                marketCap:String?,
//                country:String?,
//                 IPOyear:String?,
//                 volume:String?,
//                 sector:String?,
//                industry:String?){
//
//    }
}