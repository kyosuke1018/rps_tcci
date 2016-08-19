package com.tcci.fcservice.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Hank Han
 */
public class YahooTwStockQuoter {

    private static String YAHOO_STOCK_SERVIC_URL = "http://tw.stock.yahoo.com/w/stock?p=";

    public static StockQuote getQuote(String symbol) throws Exception {
        String compositeURL = YAHOO_STOCK_SERVIC_URL + symbol;
        URL url = new URL(compositeURL);
        InputStream is = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

//        String line = null;
//        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
//        }

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(is);

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();

        XPathExpression expr = xpath.compile("/stock/item[1]");

        Node result = (Node) expr.evaluate(document, XPathConstants.NODE);
        
        String number = result.getAttributes().getNamedItem("number").getNodeValue();
        String price = result.getAttributes().getNamedItem("price").getNodeValue();
        String upDownValue = result.getAttributes().getNamedItem("upDownValue").getNodeValue();

        StockQuote stockQuote = new StockQuote();

        Float lastValue = Float.parseFloat(price);
        Float changeAmount = Float.parseFloat(upDownValue);
        float changePercentage = (float) (Math.round(((lastValue / (lastValue - changeAmount)) - 1) * 100 * 100) / 100.0) ;

        stockQuote.setChangeAmount(changeAmount);
        stockQuote.setChangePercentage((changePercentage > 0 ? "+" : "") + String.valueOf(changePercentage) + "%");
        stockQuote.setLastValue(lastValue);


        return stockQuote;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getQuote("2498"));
        } catch (Exception ex) {
            Logger.getLogger(YahooTwStockQuoter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
