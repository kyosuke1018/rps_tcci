package com.tcci.fcservice.util;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hank Han
 */
@XmlRootElement
public class StockQuote {

    private String symbol;
    private String companyName;
    private float lastValue;
    private String lastUpdateDate;
    private String lastUpdateTime;
    private float changeAmount;
    private String changePercentage;
    private float openingValue;
    private float highValue;
    private float lowValue;
    private int volumn;

    public float getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(float changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(String changePercentage) {
        this.changePercentage = changePercentage;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public float getHighValue() {
        return highValue;
    }

    public void setHighValue(float highValue) {
        this.highValue = highValue;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public float getLastValue() {
        return lastValue;
    }

    public void setLastValue(float lastValue) {
        this.lastValue = lastValue;
    }

    public float getLowValue() {
        return lowValue;
    }

    public void setLowValue(float lowValue) {
        this.lowValue = lowValue;
    }

    public float getOpeningValue() {
        return openingValue;
    }

    public void setOpeningValue(float openingValue) {
        this.openingValue = openingValue;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getVolumn() {
        return volumn;
    }

    public void setVolumn(int volumn) {
        this.volumn = volumn;
    }

    @Override
    public String toString() {
        return "股票代號： " + this.symbol + "\n" +
                "股價: " + this.lastValue + "\n" +
                "資料最後更新日期: " + this.lastUpdateDate + "\n" +
                "資料最後更新時間: " + this.lastUpdateTime + "\n";
//                "股價漲幅值: " + this.changeAmount + "\n" +
//                "股價漲幅比: " + this.changePercentage + "\n" +
//                "開盤價: " + this.openingValue + "\n" +
//                "盤中最高價: " + this.highValue + "\n" +
//                "盤中最低價: " + this.lowValue + "\n" +
//                "買賣張數: " + this.volumn + "\n" +
//                "公司名稱: " + this.companyName + "\n";
    }

    
}
