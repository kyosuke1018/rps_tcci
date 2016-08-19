package com.tcci.fcservice.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Louisz Cheng
 */
public class YahooStockQuoter {

    private static String YAHOO_STOCK_SERVIC_URL = "http://finance.yahoo.com/d/quotes.csv?f=sl1d1t1c1ohgvp2n&e=.csv&s=";

    public static StockQuote getQuote(String symbol) throws Exception {
        String compositeURL = YAHOO_STOCK_SERVIC_URL + symbol;
        URL url = new URL(compositeURL);
        InputStream is = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

//        String line = null;
//        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
//        }

        StreamTokenizer st = new StreamTokenizer(reader);
//
        StockQuote stockQuote = new StockQuote();
//
        st.nextToken(); // symbol
        stockQuote.setSymbol(st.sval);
        if (!stockQuote.getSymbol().equals(symbol)) {
            throw new Exception("A problem occurred with the stock service");
        } else {
            st.nextToken(); // skip comma
            st.nextToken(); // quote
//            System.out.println("quote " + st.nval);
            stockQuote.setLastValue((float) st.nval);

            st.nextToken(); // skip comma
            st.nextToken(); // last update date
//            System.out.println("last update date " + st.sval);
            stockQuote.setLastUpdateDate(st.sval);

            st.nextToken(); // skip comma
            st.nextToken(); // last update time
//            System.out.println("last update time " + st.sval);
            stockQuote.setLastUpdateTime(st.sval);

            st.nextToken(); // skip comma
            st.nextToken(); // change amount
            if (st.ttype != StreamTokenizer.TT_NUMBER) {
                st.nextToken();
            }
//            System.out.println("change amount " + st.nval);
            stockQuote.setChangeAmount((float) st.nval);

            st.nextToken(); // skip comma
            st.nextToken(); // opening value
//            System.out.println("opening value" + st.nval);
            stockQuote.setOpeningValue((float) st.nval);

            st.nextToken(); // skip comma
            st.nextToken(); // high value
//            System.out.println("high value " + st.nval);
            stockQuote.setHighValue((float) st.nval);

            st.nextToken(); // skip comma
            st.nextToken(); // low value
//            System.out.println("low value " + st.nval);
            stockQuote.setLowValue((float) st.nval);

            st.nextToken(); // skip comma
            st.nextToken(); // vloumn
//            System.out.println("vloumn " + st.nval);
            stockQuote.setVolumn((int) st.nval);

            st.nextToken(); // skip comma
            st.nextToken(); // change percentage
//            System.out.println("change percentage " + st.sval);
            stockQuote.setChangePercentage(st.sval);

            st.nextToken(); // skip comma
            st.nextToken(); // company name
//            System.out.println("company name " + st.sval);
            stockQuote.setCompanyName(st.sval);

        }

        return stockQuote;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getQuote("1101.TW"));
            System.out.println(getQuote("1136.HK"));
        } catch (Exception ex) {
            Logger.getLogger(YahooStockQuoter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
