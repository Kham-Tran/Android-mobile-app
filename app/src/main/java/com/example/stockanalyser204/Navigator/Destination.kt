package com.example.stockanalyser201.Navigator

interface Destination {
    var route:String
}

object Home:Destination{
    override var route = "home"
}

object  List:Destination{
    override var route = "list"
}

object Ticket:Destination{
    override var route = "Ticket"
}

object Porfolio:Destination{
    override var route = "Porfoilo"
}
object Detector:Destination{
    override var route = "Detector"
}

object paperTrading:Destination{
    override var route = "PaperTrading"
}