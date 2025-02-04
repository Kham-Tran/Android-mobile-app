package com.example.stockanalyser201.LocalDatabase

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "GroupList")
class GroupList{
    @PrimaryKey()
//    @NonNull
    @ColumnInfo(name = "GroupName")
    var GroupName:String = ""

    constructor(GroupName:String){
        this.GroupName = GroupName
    }
}

@Entity(tableName = "Symbol", foreignKeys = arrayOf(ForeignKey(
    entity = GroupList::class,
    parentColumns = arrayOf("GroupName"),
    childColumns = arrayOf("GroupNameID"),
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE)))
//@Entity(tableName = "Symbol")
class Symbol{
//    @NonNull
//    @ColumnInfo(name = "id")
//    var id:Int = 0

    @PrimaryKey
    @ColumnInfo(name = "SName")
    var SName:String = ""

    @ColumnInfo(name = "open")
    var open:Double? = 0.0

    @ColumnInfo(name = "close")
    var close:Double? = 0.0

    @ColumnInfo(name = "high")
    var high:Double? = 0.0

    @ColumnInfo(name = "low")
    var low:Double? = 0.0

    @ColumnInfo(name = "volume")
    var volume:Long? = 0L

    @ColumnInfo(name = "date")
    var date: Long? = 0

    @ColumnInfo(name = "GroupNameID")
    var GroupNameID:String = ""

    constructor(
        SName:String="",
        open: Double?= 0.0,
        close: Double? = 0.0,
        high: Double? = 0.0,
        low: Double? = 0.0,
        volume: Long? = 0,
        date: Long? = 0,
        GroupNameID: String = "General"
    ){
        this.SName = SName
        if (open != null) {
            this.open = open
        }
        if (close != null) {
            this.close = close
        }
        if (high != null) {
            this.high = high
        }
        if (low != null) {
            this.low = low
        }
        if (volume != null) {
            this.volume = volume
        }
        this.date = date

        this.GroupNameID = GroupNameID
    }
}

@Entity(tableName = "Note", foreignKeys = arrayOf(ForeignKey(
    entity = Symbol::class,
    parentColumns = arrayOf("SName"),
    childColumns = arrayOf("noteID"),
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
    )))
class Note{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var noteID:Int = 0
    var status:String = ""
    var startDate:Long = 0
    var endDate:Long = 0
    var percentChange:Double = 0.0
    var SName:String = ""

    constructor(status:String,startDate:Long,endDate:Long,percentChange:Double,SName:String){
        this.status = status
        this.startDate = startDate
        this.endDate = endDate
        this.percentChange = percentChange
        this.SName = SName
    }
}

@Entity(tableName = "symbolER",foreignKeys = arrayOf(ForeignKey(
    entity = Symbol::class,
    parentColumns = arrayOf("SName"),
    childColumns = arrayOf("ERid"),
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
)))
class symbolER{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var ERid:Int = 0
    var ERdate:Long = 0
    var fiscalQuater:String = ""
    var forecast:Double = 0.0
    var EPS:Double = 0.0
    var SName:String = ""

    constructor(ERdate:Long,fiscalQuater:String,forecast:Double, EPS:Double,SName:String){
        this.ERdate = ERdate
        this.fiscalQuater = fiscalQuater
        this.EPS = EPS
        this.forecast = forecast
        this.SName = SName
    }
}

@Entity(tableName = "symbolNews",foreignKeys = arrayOf(ForeignKey(
    entity = Symbol::class,
    parentColumns = arrayOf("SName"),
    childColumns = arrayOf("newsID"),
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
)))
class symbolNews{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var newsID:Int = 0
    var newsDate:Long = 0
    var newsTitle:String =""
    var newsContent:String = ""
    var author:String =""
    var SName:String =""

    constructor(newsDate:Long,newsTitle:String,newsContent:String,author:String,SName: String){
        this.newsDate=newsDate
        this.newsTitle=newsTitle
        this.newsContent=newsContent
        this.author=author
        this.SName=SName
    }

}




data class GroupOfSymbol(
    @Embedded val group:GroupList,
    @Relation(
        parentColumn = "GroupName",
        entityColumn = "GroupNameID",
    )
    val symbolList:List<Symbol>

)

data class SymbolwithNotes(
    @Embedded val symbol:Symbol,
    @Relation(
        parentColumn = "SName",
        entityColumn = "SName",
    )
    val notes:List<Note>
)

data class SymbolwithER(
    @Embedded val symbol:Symbol,
    @Relation(
        parentColumn = "SName",
        entityColumn = "SName",
    )
    val notes:List<symbolER>
)

data class SymbolwithNews(
    @Embedded val symbol:Symbol,
    @Relation(
        parentColumn = "SName",
        entityColumn = "SName",
    )
    val notes:List<symbolNews>
)

@Entity(tableName = "holdingPorfolio")
data class holdingPorfolio(
    @PrimaryKey
    @ColumnInfo(name = "SName")
    var SName:String,
    var holdingShares:Double,
    var holdingPrice:Double,
    var marketPrice:Double
)

@Entity(tableName = "paperTradeSymbol")
data class paperTradeSymbol(
    @PrimaryKey
    @ColumnInfo(name = "SName")
    var SName:String,
    var holdingShares:Double,
    var holdingPrice:Double,
    var marketPrice:Double
)

