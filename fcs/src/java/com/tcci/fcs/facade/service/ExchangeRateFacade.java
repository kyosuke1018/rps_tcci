/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade.service;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCurrency;
import com.tcci.fcs.entity.FcMonthlyExchangeRate;
import com.tcci.fcs.enums.CurrencyEnum;
import com.tcci.fcs.facade.FcCurrencyFacade;
import com.tcci.fcs.facade.FcMonthlyExchangeRateFacade;
import com.tcci.fcs.model.global.GlobalConstant;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
@Path("exchangeRate")
public class ExchangeRateFacade {
    protected static final Logger logger = LoggerFactory.getLogger(ExchangeRateFacade.class);
    
    @EJB
    private FcCurrencyFacade fcCurrencyFacade;
    @EJB
    private FcMonthlyExchangeRateFacade fcMonthlyExchangeRateFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    private String WebURL = GlobalConstant.MONTHLY_EXCHANGE_RATE_WEB_URL;//外幣結帳價格表
    private String urlData;
    
    @GET
    @Path("update")
    @Produces("text/plain")
    public String update(TcUser user) {
        logger.info("update invoked.");
        try {
            List<FcMonthlyExchangeRate> resultList = this.getWebInformation();
//            for(FcMonthlyExchangeRate fcMonthlyExchangeRate:resultList){
//               fcMonthlyExchangeRateFacade .save(fcMonthlyExchangeRate, user);
//            }
            return "OK";
        } catch (Exception ex) {
            logger.error("flushCache() exception", ex);
            return "Exception:" + ex.getMessage();
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="for 匯率 web info">
    public List<FcMonthlyExchangeRate> getWebInformation(){
        // 讀取網頁資料，並將資料存放在於 urlData
        urlData = this.GetURLData();
        // 剖析 urlData，取得幣別的匯率資料
        return this.Parser(urlData);
    }
    
    public String GetURLData(){
        // 讀取網頁資料，並將資料存放在 urlData
        urlData = null;
        try {
            URL url = new URL(WebURL);
            HttpURLConnection httpUrlconnection = (HttpURLConnection) url.openConnection();
            httpUrlconnection.setDoInput(true);
            httpUrlconnection.setDoOutput(true);
            httpUrlconnection.connect();
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(httpUrlconnection.getInputStream(), "UTF-8"));
            String decodedString;
            while ((decodedString = bufReader.readLine()) != null) {
                urlData += decodedString;
            }
            bufReader.close();
            httpUrlconnection.disconnect();
        }catch(Exception e){
            logger.error("GetURLData Exception", e.toString());
        }
        return urlData;
    }
    
    public List<FcMonthlyExchangeRate> Parser( String urlData ){
        // 剖析 urlData，取得幣別的匯率資料
//        logger.debug("Parser urlData:"+ urlData);
        //取得報價年月
        int quoteDtIndex = urlData.indexOf("QuoteDt");
        String quoteYM = null;
        if(quoteDtIndex > 0){
            logger.debug("Parser urlData　quoteDtIndex:"+ quoteDtIndex);
            String quoteDt = urlData.substring(quoteDtIndex+9, quoteDtIndex+25);
            logger.debug("Parser urlData　QuoteDt:"+ quoteDt);
            quoteDt = quoteDt.trim().replaceAll(" ", "").replaceAll("年", "").replaceAll("月", "").replaceAll("日", "");
            logger.debug("Parser urlData　QuoteDt:"+ quoteDt);
            quoteYM = quoteDt.substring(0, 6);
            logger.debug("Parser urlData　quoteYM:"+ quoteYM);
        }
        
        if(quoteYM == null){
            return null;
        }
        
        //各幣別的匯率資料
        List<FcCurrency> allList = fcCurrencyFacade.findAll();
        List<FcMonthlyExchangeRate> rateList = fcMonthlyExchangeRateFacade.findByYM(quoteYM);
        List<FcMonthlyExchangeRate> resultList = new ArrayList<FcMonthlyExchangeRate>();
        for(FcCurrency currency : allList){
            if(CurrencyEnum.TWD.name().equals(currency.getCode())){//應排除台幣 或固定寫1
                continue;
            }
            //資訊寫入月結匯率檔
            FcMonthlyExchangeRate result = new FcMonthlyExchangeRate();
            boolean exist = false;
            if(rateList!=null){
                for(FcMonthlyExchangeRate fcMonthlyExchangeRate:rateList){
                    if(currency.equals(fcMonthlyExchangeRate.getCurrency())){
                        exist = true;
                        result = fcMonthlyExchangeRate;
                        break;
                    }
                }
            }
            if(!exist){
                result.setCurrency(currency);
                result.setYearmonth(quoteYM);
            }
            
            String currencyStr = "("+currency.getCode()+")";
//            logger.debug("Parser urlData　currencyStr:"+ currencyStr);
            int currencyStrIndex = urlData.indexOf(currencyStr);
            if(currencyStrIndex > 0){
                String tempStr = urlData.substring(currencyStrIndex, currencyStrIndex+60);
                logger.debug("Parser urlData　tempStr:"+ tempStr);
                
                int start =  tempStr.indexOf("<td align='left' class=\"decimal\">");
                int end =  tempStr.indexOf("</td>",start);
                String rateStr = tempStr.substring(start+33, end);
                logger.debug("Parser urlData　exchange rate:"+currencyStr+"_"+ rateStr);
                result.setRate(new BigDecimal(rateStr));
            }else{
                result.setRate(BigDecimal.ONE);//無法取得匯率的特殊幣別寫入預設值1 如印度盧比
            }
            resultList.add(result);
        }
        
        return resultList;
    }
    //</editor-fold>
    
}
