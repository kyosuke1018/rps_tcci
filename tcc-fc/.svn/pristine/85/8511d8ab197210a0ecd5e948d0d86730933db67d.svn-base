package com.tcci.fcservice.util;

//import com.sun.org.apache.xpath.internal.NodeSet;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
//import org.w3c.dom.Document;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Louisz.Cheng
 */
public class YahooWeather {

    private static String YAHOO_WEATHER_SERVIC_URL = "http://weather.yahooapis.com/forecastrss?u=c&w=";

    public static WeatherQuote getQuote(String symbol) throws Exception {
        String compositeURL = YAHOO_WEATHER_SERVIC_URL +symbol;
        WeatherQuote weather = new WeatherQuote();
        URL url = new URL(compositeURL);
        InputStream is = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(is);
     
        
//        System.out.println(node.getFirstChild().getNodeName());
        
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        
        XPathExpression expr = xpath.compile("/rss/channel/item");
//        XPathExpression expr = xpath.compile("/rss/channel/item/li");
        
        Node result = (Node) expr.evaluate(document, XPathConstants.NODE);

         NodeList nodeList = result.getChildNodes();
         for(int i =0;i<nodeList.getLength();i++){
             Node n = nodeList.item(i);
             if(n.getNodeName().equalsIgnoreCase("yweather:condition")){
                 NamedNodeMap map = n.getAttributes();
//                 System.out.println(map.getNamedItem("text"));
                 // TODO Need to fix enums
                 weather.setCity("台北市");                 
                 weather.setWeatherStatus(map.getNamedItem("text").getTextContent());
                 weather.setTemperature(map.getNamedItem("temp").getTextContent());
                 weather.setLastUpdate(map.getNamedItem("date").getTextContent());
                  weather.setCode(map.getNamedItem("code").getTextContent());
             }
              if(n.getNodeName().equalsIgnoreCase("link")){
                   weather.setRefLink(n.getTextContent());
              }
         }
   
        return weather;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getQuote("2306179"));
//            System.out.println(getQuote("1136.HK"));
        } catch (Exception ex) {
            Logger.getLogger(YahooWeather.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
