package com.example.stockanalyser201.LocalDatabase

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//import com.example.stockanalyser201.LocalDatabase.AllAttributesWithSymbolDAO
//import com.example.stockanalyser201.LocalDatabase.AllGroupAndSymbolDAO
//import com.example.stockanalyser201.LocalDatabase.GroupDAO
//import com.example.stockanalyser201.LocalDatabase.GroupList
//import com.example.stockanalyser201.LocalDatabase.Note
//import com.example.stockanalyser201.LocalDatabase.Symbol
//import com.example.stockanalyser201.LocalDatabase.SymbolDAO
//import com.example.stockanalyser201.LocalDatabase.holdingPorfolio
//import com.example.stockanalyser201.LocalDatabase.paperTradeSymbol
//import com.example.stockanalyser201.LocalDatabase.porfolioDAO
//import com.example.stockanalyser201.LocalDatabase.symbolER
//import com.example.stockanalyser201.LocalDatabase.symbolNews

//autoMigrations = [AutoMigration(from = 1, to = 2)]

@Database(entities = [(GroupList::class),(Symbol::class),(Note::class),(symbolER::class),(symbolNews::class),(holdingPorfolio::class),(paperTradeSymbol::class)], version = 2, exportSchema = true,autoMigrations = [AutoMigration(from = 1, to = 2)])
abstract class KHDataBase:RoomDatabase() {

        abstract fun symbolDAO(): SymbolDAO
        abstract  fun groupDAO(): GroupDAO
        abstract  fun groupOfsymbol(): AllGroupAndSymbolDAO
        abstract  fun symbolOfattributes(): AllAttributesWithSymbolDAO
        abstract  fun porfolioDao(): porfolioDAO
        abstract  fun paperTradeDAO():paperTradeDAO
        companion object{

            private var INSTANCE:KHDataBase? = null
            fun getInstance(context: Context): KHDataBase {
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(

                        context.applicationContext,
                        KHDataBase::class.java,"StockAnalysisDB"
                    ).build()
                    INSTANCE = instance
                }

                return instance

            }
        }

}