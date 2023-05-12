package com.example.stockmobile;

import org.junit.Test;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;
import static org.junit.Assert.*;
import com.example.stockmobile.dataRetriever.YHfinance;

import java.io.IOException;
import java.util.Calendar;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws IOException {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH,5);
        YHfinance spy = new YHfinance("NVDA");
        assertEquals("NVDA",spy.getSymbol());
        System.out.println(date.getTime());
        //System.out.println(spy.getData(Interval.DAILY).getHistory().get(0));
    }
}