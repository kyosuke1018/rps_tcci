/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

/**
 *
 * @author Louisz.Cheng
 */
@Singleton
public class PageHeaderInfoUtils {
    private  StockQuote twSq;
    private  StockQuote hkSq;
    private  StockQuote twSq2104;
    private  StockQuote twSq4725;
    private  WeatherQuote weatherQ;
    
    @PostConstruct
    public void init(){
        twSq = new StockQuote();
        hkSq = new StockQuote();
        twSq2104 = new StockQuote();
        twSq4725 = new StockQuote();
        weatherQ = new WeatherQuote();       
        
    }
    
    public StockQuote getHkSq() {
        return hkSq;
    }

    public void setHkSq(StockQuote hkSq) {
        this.hkSq = hkSq;
    }

    public StockQuote getTwSq() {
        return twSq;
    }

    public void setTwSq(StockQuote twSq) {
        this.twSq = twSq;
    }

    public StockQuote getTwSq2104() {
        return twSq2104;
    }

    public void setTwSq2104(StockQuote twSq2104) {
        this.twSq2104 = twSq2104;
    }

    public StockQuote getTwSq4725() {
        return twSq4725;
    }

    public void setTwSq4725(StockQuote twSq4725) {
        this.twSq4725 = twSq4725;
    }

    public WeatherQuote getWeatherQ() {
        return weatherQ;
    }

    public void setWeatherQ(WeatherQuote weatherQ) {
        this.weatherQ = weatherQ;
    }
}
