/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class CoreFacade {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 匯率轉換
     * @param priceSrc
     * @param curSrc
     * @param curDes
     * @param fxDate
     * @return 
     */
    public BigDecimal calFxPrice(BigDecimal priceSrc, String curSrc, String curDes, Date fxDate){
        if( priceSrc==null || curSrc==null || curDes==null ){
            logger.error("calFxPrice priceSrc==null || curSrc==null || curDes==null !");
            return null;
        }
        // TODO get fxrate
        BigDecimal fxRate = getFxRate(curSrc, curDes, fxDate);
        
        return priceSrc.multiply(fxRate);
    }
    
    /**
     * 取得匯率
     * @param curSrc
     * @param curDes
     * @param fxDate
     * @return 
     */
    public BigDecimal getFxRate(String curSrc, String curDes, Date fxDate){
        // TODO get fxrate
        return BigDecimal.ONE;
    }
    
    // TODO 廠辦、杭州辦、處辦 判斷
    
    // TODO 可否異動 RFQ 時間點判斷
    
    // TODO 可否異動 報價 時間點判斷
    
    // TODO 可否異動 決標 時間點判斷

    // TODO 自動開標 判斷
    
    // TODO 投標/報價截止判斷
    
}
