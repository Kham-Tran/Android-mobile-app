package com.example.stockanalyser201.LocalDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SymbolDAO {
    @Query("SELECT * FROM Symbol")
    fun getallTickets(): List<Symbol>

    @Query("SELECT * FROM Symbol WHERE SName = :SName")
    fun getTicket(SName:String): List<Symbol>

    @Query("SELECT * FROM Symbol WHERE GroupNameID == :GroupName")
    fun getTicketByGroup(GroupName:String): List<Symbol>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tickets:List<Symbol>)

    @Query("UPDATE Symbol SET open = :open, close=:close,high=:high,low=:low,volume=:volume,date=:date  WHERE SName = :SName AND GroupNameID = :group")
    fun updateTicket(
        open:Double?,
        close:Double?,
        high:Double?,
        low:Double?,
        volume: Long?,
        date:Long?,
        SName:String,
        group:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(symbol:Symbol)


    @Query("DELETE FROM Symbol WHERE SName = :SName")
    fun deleteSymbol(SName: String)

    @Query("DELETE FROM Symbol WHERE SName = :SName AND GroupNameID = :group")
    fun deleteSymbolinGroup(SName: String, group:String)

    @Delete
    fun deleteAll(tickets: List<Symbol>)

    @Query("SELECT EXISTS(SELECT * FROM Symbol WHERE SName = :SName AND GroupNameID = :GroupNameID)")
    fun isExists(SName: String, GroupNameID:String): Boolean

}

@Dao
interface GroupDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(Groups:List<GroupList>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(Groups:GroupList)

    @Query("SELECT * FROM GroupList")
    fun getallGroup(): List<GroupList>

    @Query("SELECT * FROM GroupList WHERE GroupName = :GroupName")
    fun getTicket(GroupName:String): GroupList

    @Delete
    fun deleteAll(tickets: List<GroupList>)

    @Query("DELETE FROM GroupList WHERE GroupName = :GroupName")
    fun deleteGroup(GroupName: String)

    @Query("SELECT EXISTS(SELECT * FROM GroupList WHERE GroupName = :GroupName)")
    fun isExists(GroupName: String): Boolean

}



@Dao
interface AllGroupAndSymbolDAO{
    @Transaction
    @Query("SELECT * FROM GroupList")
    fun getallGroupAndSymbol():List<GroupOfSymbol>

    @Transaction
    @Query("SELECT * FROM GroupList WHERE GroupName = :GroupName")
    fun getOneGroupAndSymbol(GroupName: String):List<GroupOfSymbol>
}

@Dao
interface AllAttributesWithSymbolDAO{
    @Transaction
    @Query("SELECT * FROM Symbol WHERE SName = :SName")
    fun getAllNotes(SName: String):List<SymbolwithNotes>

    @Transaction
    @Query("SELECT * FROM Symbol WHERE SName = :SName")
    fun getAllER(SName: String):List<SymbolwithER>

    @Transaction
    @Query("SELECT * FROM Symbol WHERE SName = :SName")
    fun getAllNews(SName: String):List<SymbolwithNews>
}


@Dao
interface porfolioDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(porfolio: holdingPorfolio)

    @Query("SELECT * FROM holdingPorfolio")
    fun getallGroup(): List<holdingPorfolio>

    @Query("SELECT * FROM holdingPorfolio WHERE SName = :SName")
    fun getTicket(SName:String): holdingPorfolio

    @Delete
    fun deleteAll(tickets: List<holdingPorfolio>)

    @Query("DELETE FROM holdingPorfolio WHERE SName = :SName")
    fun deleteItem(SName: String)

    @Query("SELECT EXISTS(SELECT * FROM holdingPorfolio WHERE SName = :SName)")
    fun isExists(SName: String): Boolean

    @Query("UPDATE holdingPorfolio SET holdingPrice =:holdingPrice, holdingShares = :holdingShares, marketPrice = :marketPrice WHERE SName = :SName")
    fun updateOne(SName: String,holdingShares:Double,
                  holdingPrice:Double,
                  marketPrice:Double)
}
@Dao
interface paperTradeDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(paperTrade: paperTradeSymbol)

    @Query("SELECT * FROM paperTradeSymbol")
    fun getallGroup(): List<paperTradeSymbol>

    @Query("SELECT * FROM paperTradeSymbol WHERE SName = :SName")
    fun getTicket(SName:String): paperTradeSymbol

    @Delete
    fun deleteAll(papertradeItem:List<paperTradeSymbol>)

    @Query("DELETE FROM paperTradeSymbol WHERE SName = :SName")
    fun deleteItem(SName: String)

    @Query("SELECT EXISTS(SELECT * FROM paperTradeSymbol WHERE SName = :SName)")
    fun isExists(SName: String): Boolean

    @Query("UPDATE paperTradeSymbol SET holdingPrice =:holdingPrice, holdingShares = :holdingShares, marketPrice = :marketPrice WHERE SName = :SName")
    fun updateOne(SName: String,
                  holdingShares:Double,
                  holdingPrice:Double,
                  marketPrice:Double)
}


