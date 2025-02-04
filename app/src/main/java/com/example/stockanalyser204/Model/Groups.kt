package com.example.stockanalyser201.Model

import com.example.stockanalyser201.LocalDatabase.GroupList


data class Groups(
    var groups:List<GroupList>
)

data class Group(
    var name:String,
    var list:List<stock_info>
)

