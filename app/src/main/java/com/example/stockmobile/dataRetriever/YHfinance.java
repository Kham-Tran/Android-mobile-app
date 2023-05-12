package com.example.stockmobile.dataRetriever;


import java.io.IOException;
import java.util.Calendar;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;


public class YHfinance {
    private String symbol;
    public YHfinance(String symbol){
        this.symbol = symbol;
    }

    private Stock getSymbolByInterval(Interval interval) {
        try{
            return YahooFinance.get(this.symbol);
        }
        catch (Exception e){
            System.out.println("Can't process request");
            return null;
        }
    }

    private Stock getSymbolByDateFrom(Interval interval, Calendar calendar) throws IOException {
        return  YahooFinance.get(this.symbol, calendar, interval);
    }

    private  Stock getSymbolByDateFromTo(Interval interval, Calendar from, Calendar to) throws IOException {
        return YahooFinance.get(this.symbol,from,to,interval);
    }

    public Stock getData(Interval interval) throws IOException{
        return this.getData(interval);
    }

    public Stock getDataFrom(Interval interval, Calendar from) throws IOException {
        return this.getSymbolByDateFrom(interval,from);
    }

    public Stock getDataFromTo(Interval interval, Calendar from, Calendar to) throws IOException {
        return this.getSymbolByDateFrom(interval,from);
    }

    public  String getSymbol(){
        return this.symbol;
    }

}