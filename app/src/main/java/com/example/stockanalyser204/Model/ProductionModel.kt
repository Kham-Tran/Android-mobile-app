package com.example.stockanalyser201.Model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.stockanalyser201.LocalDatabase.GroupList
import com.example.stockanalyser201.LocalDatabase.GroupOfSymbol

import com.example.stockanalyser201.LocalDatabase.KHDataBase
import com.example.stockanalyser201.LocalDatabase.Symbol
import com.example.stockanalyser201.LocalDatabase.holdingPorfolio
import com.example.stockanalyser201.LocalDatabase.paperTradeSymbol

import com.example.stockanalyser201.Network.NetworkAPI
import com.example.stockanalyser201.Network.Ticket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductionModel(context: Context, value: Boolean):ViewModel() {
    var stockInfo:List<stock_info>
    val db = KHDataBase.getInstance(context)
    val groupDAO = db.groupDAO()
    val symbolDAO = db.symbolDAO()
    val groupOfSymbolDAO = db.groupOfsymbol()
    val attrubutesOfsymbol = db.symbolOfattributes()
    val porfolioDao = db.porfolioDao()
    val paperTradeDao = db.paperTradeDAO()


    val client = NetworkAPI(context)
    var _TicketStateFlow = MutableStateFlow<List<GroupOfSymbol>>(emptyList<GroupOfSymbol>())
    var TicketStateFlow:StateFlow<List<GroupOfSymbol>> = _TicketStateFlow

    lateinit var listsymbol:List<Symbol>
    var _listSymbol = MutableStateFlow<List<Symbol>>(emptyList<Symbol>())
    var listSymbol:StateFlow<List<Symbol>> = _listSymbol
    var _messege = MutableStateFlow("")
    var messege:StateFlow<String> = _messege

    var _listGroup = MutableStateFlow<List<GroupList>>(emptyList<GroupList>())
    var listgroup:StateFlow<List<GroupList>> = _listGroup

//    var _symbol = MutableStateFlow<Symbol>(value = Symbol())
//    var symbol:StateFlow<Symbol> = _symbol

    var _graphData = MutableStateFlow<Ticket>(Ticket())
    var grapData:StateFlow<Ticket> = _graphData

    var _volumeData = MutableStateFlow<List<Long>>(emptyList<Long>())
    var volumeData: StateFlow<List<Long>> = _volumeData

    var _tickerData = MutableStateFlow<List<String>>(emptyList<String>())
    var tickerData: StateFlow<List<String>> = _tickerData

    var _timeData = MutableStateFlow<List<Long>>(emptyList<Long>())
    var timeData: StateFlow<List<Long>> = _timeData
//
    var _porfolio = MutableStateFlow<List<holdingPorfolio>>(emptyList<holdingPorfolio>())
    var porfolio :StateFlow<List<holdingPorfolio>> = _porfolio

    var _pumpData = MutableStateFlow<List<Ticket>>(emptyList<Ticket>())
    var pumpData:StateFlow<List<Ticket>> = _pumpData

    lateinit var sector:MutableSet<String>

   init {
           // Code retrieve list of available stock market
              stockInfo = context.assets.open("nasdaq_screener.csv")
               .bufferedReader()
               .use {
               it.readLines().map { item ->
                   var data = item.split(",")
                   stock_info(data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8],data[9],data[10])

               }
           }
       getInitPriceList()
       getListGroup()
       getPorfolio()
       getPaperTradeList()
   }


    // get all group in database
    fun getListGroup(){
        CoroutineScope(Dispatchers.Default).launch {
            var data = groupDAO.getallGroup()
            if(!data.isEmpty()) {
                _listGroup.emit(data)
            }
            else{
                _listGroup.emit(data)
            }
        }
    }

    //
    fun getInitPriceList() {
        CoroutineScope(Dispatchers.Default).launch {
            var allGroup = groupOfSymbolDAO.getallGroupAndSymbol()
           val volumeList: MutableList<Long> = emptyList<Long>().toMutableList()
            val tickerList: MutableList<String> = emptyList<String>().toMutableList()
            val timeList: MutableList<Long> = emptyList<Long>().toMutableList()
//            val allItem = groupDAO.getallGroup()
//            groupDAO.deleteAll(allItem)

            if (!allGroup.isEmpty()) {
                for (group in allGroup) {
                    for (symbol in group.symbolList) {
//                        CoroutineScope(Dispatchers.Default).launch {
                            var ticket = client.getTicketsPrice(symbol.SName)
//                            if (ticket.error == null) {
                                symbolDAO.updateTicket(
                                    ticket.open.last(),
                                    ticket.close.last(),
                                    ticket.high.last(),
                                    ticket.low.last(),
                                    ticket.volume.last(),
                                    ticket.date.last(),
                                    ticket.SName,
                                    group.group.GroupName
                                )
//                            }
                        volumeList.add(ticket.volume.maxOf { it })
                        tickerList.add(ticket.SName)
                        ticket.date[ticket.volume.indexOf(ticket.volume.max())]?.let {
                            timeList.add(
                                it
                            )
                        }
                    }
                }
                var list = groupOfSymbolDAO.getallGroupAndSymbol()
                    _TicketStateFlow.emit(list)
                    _volumeData.emit(volumeList)
                    _tickerData.emit(tickerList)
                    _timeData.emit(timeList)
            }


        }
    }

//    fun refresh(grouplist:GroupOfSymbol){
//        CoroutineScope(Dispatchers.Default).launch {
//            for (symbol in grouplist.symbolList){
//                var ticket = NetworkAPI().getTicketsPrice(symbol.SName)
////                if (ticket.error == null) {
//                    symbolDAO.updateTicket(
//                        ticket.open.last(),
//                        ticket.close.last(),
//                        ticket.high.last(),
//                        ticket.low.last(),
//                        ticket.volume.last(),
//                        ticket.date.last(),
//                        ticket.SName,
//                        grouplist.group.GroupName
//                    )
////                }
//            }
//            _messege.emit(grouplist.group.GroupName)
//        }
//
//    }

    //
    fun getsymbols(groupName:String){
        CoroutineScope(Dispatchers.Default).launch {
            listsymbol = symbolDAO.getTicketByGroup(groupName)
            _listSymbol.emit(listsymbol)
            _messege.emit("")
        }
    }

    //
    fun addSymbol(symbol:String, group:String) {
        CoroutineScope(Dispatchers.Default).launch {
            if (symbolDAO.isExists(symbol, group) and groupDAO.isExists(group)) {

                    var ticket = client.getTicketsPrice(symbol)
//                    if (ticket.error == null) {
                        symbolDAO.updateTicket(
                            ticket.open.last(),
                            ticket.close.last(),
                            ticket.high.last(),
                            ticket.low.last(),
                            ticket.volume.last(),
                            ticket.date.last(),
                            ticket.SName,
                            group
                        )

            } else if (!symbolDAO.isExists(symbol, group) and groupDAO.isExists(group)) {

                    var ticket = client.getTicketsPrice(symbol)
//                    if (ticket.error == null) {
                        symbolDAO.insertOne(
                            Symbol(
                                ticket.SName,
                                ticket.open.last(),
                                ticket.close.last(),
                                ticket.high.last(),
                                ticket.low.last(),
                                ticket.volume.last(),
                                ticket.date.last(),
                                group
                            )
                        )



            } else if (symbolDAO.isExists(symbol, group) and !groupDAO.isExists(group)) {

                    var ticket = client.getTicketsPrice(symbol)
                        symbolDAO.updateTicket(
                            ticket.open.last(),
                            ticket.close.last(),
                            ticket.high.last(),
                            ticket.low.last(),
                            ticket.volume.last(),
                            ticket.date.last(),
                            ticket.SName,
                            group
                        )
                        groupDAO.insertOne(GroupList(group))



            } else if (!symbolDAO.isExists(symbol, group) and !groupDAO.isExists(group)) {

                    var ticket = client.getTicketsPrice(symbol)
                   groupDAO.insertOne(GroupList(group))
//                    if (ticket.error == null) {
                        symbolDAO.insertOne(
                            Symbol(
                                ticket.SName,
                                ticket.open.last(),
                                ticket.close.last(),
                                ticket.high.last(),
                                ticket.low.last(),
                                ticket.volume.last(),
                                ticket.date.last(),
                                group
                            )
                        )

//                    } else {
//
//                    }
            }
            else{

            }
            getInitPriceList()
            getListGroup()
            }
    }

    //
    fun deleSymbol(symbol:String,group:String){
        CoroutineScope(Dispatchers.Default).launch {
            if (symbolDAO.isExists(symbol, group) and groupDAO.isExists(group)) {
                symbolDAO.deleteSymbolinGroup(symbol,group)
                _messege.emit(group)
            }
        }
    }

    fun deleteGroup(group:String){
        CoroutineScope(Dispatchers.Default).launch {
            if (groupDAO.isExists(group)) {
                groupDAO.deleteGroup(group)
                getInitPriceList()
                getListGroup()
            }
        }
    }

    fun getGraphData(name:String, interval:String, range:String) {
        CoroutineScope(Dispatchers.Default).launch {
            _graphData.emit(client.getTicketsPrice(name = name, interval = interval, range = range))
        }
    }


    fun setMessege(messege:String){
        CoroutineScope(Dispatchers.Default).launch {
            _messege.emit(messege)
        }

    }

    fun addPorfolioItem(SName:String,
                        Shares:Double,
                        Price:Double = 0.0,
                        buyORsell:String,
                        sharesORdollar:String,
                        marketPrice:Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            var onMarketPrice = client.getTicketsPrice(SName)
            var data = holdingPorfolio(SName,0.0,0.0,0.0)
            var isexist = porfolioDao.isExists(SName = SName)
            if(isexist){
                data = porfolioDao.getTicket(SName)
            }
            var accountValue = paperTradeDao.getTicket("Account")
            var newShares = 0.0
            var newPrice = 0.0
            var newAccountValue = 0.0

            if(isexist){

            }
            else{
                println("ticker not exist")
                if(buyORsell.equals("BUY")){
                    println("buying")
                    onMarketPrice = client.getTicketsPrice(SName)
                    // BUY Algorithm
                    if (!marketPrice){
                        println("buying not market")
                        if (sharesORdollar.equals("SHARES")){
                            newShares = data.holdingShares + Shares
                            newPrice  = ((data.holdingPrice * data.holdingShares)+(Price*Shares))/newShares
                            newAccountValue -= Price * Shares
                            println(newAccountValue)

                        }else if(sharesORdollar.equals("DOLLAR"))
                        {
                            newShares = data.holdingShares + (Shares/onMarketPrice.close.last())
                            newPrice = ((data.holdingPrice * data.holdingShares)+(Price*(Shares/onMarketPrice.close.last())))/newShares
                            newAccountValue -= (Price*(Shares/onMarketPrice.close.last()))
                        }

                    }
                    else{
                        println("buying market")
                        if (sharesORdollar.equals("SHARES") ){
                            println("buying market Shares")
                            newShares = data.holdingShares + Shares
                            newPrice  = ((data.holdingPrice * data.holdingShares)+(onMarketPrice.close.last()*Shares))/newShares
                            newAccountValue -= onMarketPrice.close.last() * Shares
                        }
                        else if(sharesORdollar.equals("DOLLAR"))
                        {
                            println("buying market Dollar")
                            newShares = data.holdingShares + (Shares/onMarketPrice.close.last())
                            newPrice = ((data.holdingPrice * data.holdingShares)+(onMarketPrice.close.last()*(Shares/onMarketPrice.close.last())))/newShares
                            newAccountValue -= onMarketPrice.close.last()*(Shares/onMarketPrice.close.last())
                            println(newAccountValue)
                        }
                    }

                }
                //
                if(newAccountValue+accountValue.holdingPrice >= 0){
                    paperTradeDao.insertOne(paperTradeSymbol(SName,newShares,newPrice,onMarketPrice.close.last()))
                    paperTradeDao.updateOne("Account",0.0,newAccountValue+accountValue.holdingPrice,0.0)
                }

            }
            }
            getPorfolio()
        }



    fun deletePorfolioItem(SName: String){
        porfolioDao.deleteItem(SName)
    }

    fun refreshPorfolio(){
        CoroutineScope(Dispatchers.Default).launch {
            var list = porfolioDao.getallGroup()
            list.forEach{
                    item ->
                if(item.SName!="Account"){
                var data = client.getTicketsPrice(item.SName)
                porfolioDao.updateOne(item.SName,item.holdingShares,item.holdingPrice,data.close.last())}
            }
            getPorfolio()
        }
    }

    fun getPorfolio(){
        CoroutineScope(Dispatchers.IO).launch {
            if(!porfolioDao.isExists("Account")){
               porfolioDao.insertOne(holdingPorfolio("Account", 0.0,0.0,0.0))
                //porfolioDao.deleteItem("Account Value")
            }
            _porfolio.emit(porfolioDao.getallGroup())
        }
    }

    fun porfolioValue(list:List<holdingPorfolio>):Triple<Double,Double,Double> {
        var totalValue: Double = 0.0
        var totalGainLost: Double = 0.0
        var budgetValue: Double = 0.0
        list.forEach {
                item ->
            totalValue += (item.holdingShares*item.holdingPrice)
            budgetValue += (item.holdingShares*item.marketPrice)
        }
        totalGainLost = budgetValue - totalValue
        return Triple(totalValue,budgetValue,totalGainLost)
    }



    fun pumpDetec(keyword:String){
        var list = stockInfo.filter { it.sector?.uppercase()?.contains(keyword) ?: true  }
        CoroutineScope(Dispatchers.Default).launch {
        }
    }

    fun refreshPaperTrading(){
        CoroutineScope(Dispatchers.Default).launch {
            var list = paperTradeDao.getallGroup()
            list.forEach{
                    item ->
                if(item.SName!="Account"){
                    var data = client.getTicketsPrice(item.SName)
                    paperTradeDao.updateOne(item.SName,item.holdingShares,item.holdingPrice,data.close.last())}
            }
            getPaperTradeList()
        }
    }

    fun paperTradeValue(list:List<paperTradeSymbol>):Triple<Double,Double,Double>{
        var totalValue: Double = 0.0
        var totalGainLost: Double = 0.0
        var budgetValue: Double = 0.0
        list.forEach {
                item ->
            totalValue += (item.holdingShares*item.holdingPrice)
            budgetValue += (item.holdingShares*item.marketPrice)
        }
        totalGainLost = budgetValue - totalValue
        return Triple(totalValue,budgetValue,totalGainLost)
    }

    var _paperTrade = MutableStateFlow<List<paperTradeSymbol>>(emptyList<paperTradeSymbol>())
    var paperTrade :StateFlow<List<paperTradeSymbol>> = _paperTrade

    fun getPaperTradeList(){
        CoroutineScope(Dispatchers.IO).launch {
            if(!paperTradeDao.isExists("Account")){
                paperTradeDao.insertOne(paperTradeSymbol("Account", 0.0,5000.0,0.0))
                //porfolioDao.deleteItem("Account Value")
            }
            _paperTrade.emit(paperTradeDao.getallGroup())
        }
    }

    fun addpaperTradeItem(
        SName: String,
        Shares: Double = 0.0,
        Price: Double = 0.0,
        buyORsell: String = "",
        sharesORdollar: String = "",
        marketPrice: Boolean = false,
        sellAll: Boolean
    ){
        CoroutineScope(Dispatchers.IO).launch {
            var onMarketPrice = Ticket()
            var accountValue = paperTradeDao.getTicket("Account")
            var data = paperTradeSymbol(SName,0.0,0.0,0.0)
            var isexist = paperTradeDao.isExists(SName = SName)

            var newShares = 0.0
            var newPrice = 0.0
            var newAccountValue = 0.0
            var allItem = paperTradeDao.getallGroup()

//            println(onMarketPrice.close.last())
            if(SName.uppercase() == "RESET"){
                paperTradeDao.deleteAll(allItem)
                paperTradeDao.insertOne(paperTradeSymbol("Account",0.0,5000.0,0.0))
            }else{
                println(" Not reset")
                onMarketPrice = client.getTicketsPrice(SName)
                if(isexist){
                    println("ticker exist")
                    data = paperTradeDao.getTicket(SName)
                    if(buyORsell.equals("BUY")){
                        println("buying")
                        // BUY Algorithm
                        if (!marketPrice){
                            println("buying not market")
                            if (sharesORdollar == "Shares"){
                                newShares = data.holdingShares + Shares
                                newPrice  = ((data.holdingPrice * data.holdingShares)+(Price*Shares))/newShares
                                newAccountValue -= Price * Shares

                            }else{
                                newShares = data.holdingShares + (Shares/onMarketPrice.close.last())
                                newPrice = ((data.holdingPrice * data.holdingShares)+(Price*(Shares/onMarketPrice.close.last())))/newShares
                                newAccountValue -= (Price*(Shares/onMarketPrice.close.last()))
                            }

                        }
                        else{
                            println("buying market")
                            if (sharesORdollar == "Shares"){
                                newShares = data.holdingShares + Shares
                                newPrice  = ((data.holdingPrice * data.holdingShares)+(onMarketPrice.close.last()*Shares))/newShares
                                newAccountValue -= onMarketPrice.close.last()*Shares
                            }
                            else
                            {
                                newShares = data.holdingShares + (Shares/onMarketPrice.close.last())
                                newPrice = ((data.holdingPrice * data.holdingShares)+(onMarketPrice.close.last()*(Shares/onMarketPrice.close.last())))/newShares
                                newAccountValue -= onMarketPrice.close.last()*(Shares/onMarketPrice.close.last())
                            }
                        }

                    }else{
                        // SELL Algorithm
                        println("selling")
                        if(!marketPrice){
                            println("selling not market")
                            if (sharesORdollar == "SHARES"){
                                newShares = data.holdingShares - Shares
                                newPrice  = ((data.holdingPrice * data.holdingShares) - (Price*Shares))/newShares
                                newAccountValue += Price*Shares
                            }
                            else if(sharesORdollar == "DOLLAR"){
                                newShares = data.holdingShares - (Shares/onMarketPrice.close.last())
                                newPrice = ((data.holdingPrice * data.holdingShares) - (Price*(Shares/onMarketPrice.close.last())))/newShares
                                newAccountValue += (Price*(Shares/onMarketPrice.close.last()))
                            }
                        }
                        else{
                            println("selling market")
                            if (sharesORdollar == "SHARES"){
                                println("selling market by Shares")
                                newAccountValue += if (Shares<=data.holdingShares) onMarketPrice.close.last()*Shares else 0.0
                                newShares = data.holdingShares - Shares
                                newPrice  = data.holdingPrice

                            }
                            else if(sharesORdollar == "DOLLAR"){
                                println("selling market by Dollar")

                                if (sellAll){
                                    newAccountValue += data.holdingShares * data.holdingPrice
                                    newShares = 0.0
                                    newPrice = 0.0
                                }
                                else{
                                    newAccountValue += Shares
                                    newShares = data.holdingShares - (Shares/onMarketPrice.close.last())
                                    newPrice = data.holdingPrice
                                }
                            }
                        }
                    }

                if(buyORsell == "BUY"){
                    if(newAccountValue+accountValue.holdingPrice >= 0){
                        paperTradeDao.updateOne(SName,newShares,newPrice, onMarketPrice.close.last())
                        paperTradeDao.updateOne("Account",0.0,newAccountValue+accountValue.holdingPrice,0.0)

                    }
                }
                else{
                    if(newPrice==0.0 && newShares == 0.0){
                        paperTradeDao.updateOne("Account",0.0,newAccountValue+accountValue.holdingPrice,0.0)
                        paperTradeDao.deleteItem(SName = SName)
                    }
                    else if(newAccountValue > 0){
                        paperTradeDao.updateOne(SName,newShares,newPrice, onMarketPrice.close.last())
                        paperTradeDao.updateOne("Account",0.0,newAccountValue+accountValue.holdingPrice,0.0)
                    }
                }

            }
            else if(!isexist){
                println("ticker not exist")
                    if(buyORsell.equals("BUY")){
                        println("buying")
                        onMarketPrice = client.getTicketsPrice(SName)
                        // BUY Algorithm
                        if (!marketPrice){
                            println("buying not market")
                            if (sharesORdollar.equals("SHARES")){
                                newShares = data.holdingShares + Shares
                                newPrice  = ((data.holdingPrice * data.holdingShares)+(Price*Shares))/newShares
                                newAccountValue -= Price * Shares
                                println(newAccountValue)

                            }else if(sharesORdollar.equals("DOLLAR"))
                            {
                                newShares = data.holdingShares + (Shares/onMarketPrice.close.last())
                                newPrice = ((data.holdingPrice * data.holdingShares)+(Price*(Shares/onMarketPrice.close.last())))/newShares
                                newAccountValue -= (Price*(Shares/onMarketPrice.close.last()))
                            }

                        }
                        else{
                            println("buying market")
                            if (sharesORdollar.equals("SHARES") ){
                                println("buying market Shares")
                                newShares = data.holdingShares + Shares
                                newPrice  = ((data.holdingPrice * data.holdingShares)+(onMarketPrice.close.last()*Shares))/newShares
                                newAccountValue -= onMarketPrice.close.last() * Shares
                            }
                            else if(sharesORdollar.equals("DOLLAR"))
                            {
                                println("buying market Dollar")
                                newShares = data.holdingShares + (Shares/onMarketPrice.close.last())
                                newPrice = ((data.holdingPrice * data.holdingShares)+(onMarketPrice.close.last()*(Shares/onMarketPrice.close.last())))/newShares
                                newAccountValue -= onMarketPrice.close.last()*(Shares/onMarketPrice.close.last())
                                println(newAccountValue)
                            }
                        }

                    }
                    //
                if(newAccountValue+accountValue.holdingPrice >= 0){
                    paperTradeDao.insertOne(paperTradeSymbol(SName,newShares,newPrice,onMarketPrice.close.last()))
                    paperTradeDao.updateOne("Account",0.0,newAccountValue+accountValue.holdingPrice,0.0)
                }

            }


            }


            getPaperTradeList()
        }

    }

}