/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.schedule;

import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author penpl
 */
@Singleton
public class ScheduleFacade {
    private final static Logger logger = LoggerFactory.getLogger(ScheduleFacade.class);
    
    @Inject
    private TcScheduleFacade scheduleFacade;
    
    @Schedule(dayOfWeek = "*", hour = "*", minute = "0", persistent = false)
    public void batchNotify() {
        try {
            // by server IP 最後一碼決定 waitting N seconds. (避免 LOG 每天出現 java.sql.SQLException: ORA-00054: 資源正被使用中,.. 錯誤!)
            waittingRandomTime();

            // 3分鐘內不要再執行
            String scheduleName = "ALIVE_CHECK";
            if( scheduleFacade.canExecute(scheduleName, 3) ){
                
            } else {
                logger.warn("batchNotify not execute : "+scheduleName);
            }
        } catch (Exception ex) {
            logger.error("batchNotify exception:\n", ex.getMessage());
        }
    }
    
    /**
     * by server IP 最後一碼決定 waitting N seconds. 避免 LOG 每天出現
     * java.sql.SQLException: ORA-00054: 資源正被使用中,.. 錯誤!
     */
    private void waittingRandomTime() {
        try {
            // waitting N seconds. (避免 LOG 每天出現 java.sql.SQLException: ORA-00054: 資源正被使用中,.. 錯誤!
            InetAddress me = InetAddress.getLocalHost();
            String dottedQuad = me.getHostAddress();
            logger.debug("waittingRandomTime : dottedQuad = " + dottedQuad);
            int i = dottedQuad.lastIndexOf(".");
            logger.debug("waittingRandomTime : dottedQuad.lastIndexOf(\".\") = " + i);
            int seconds = (i <= 0) ? 0 : Integer.parseInt(dottedQuad.substring(i + 1));// 取IP最後一碼
            seconds = seconds % 60;
            logger.info("waittingRandomTime seconds = " + seconds);

            //TimeUnit.SECONDS.sleep(getRandomNumberInRange(seconds, seconds + 5)); // 等待 N 秒
            TimeUnit.SECONDS.sleep(seconds); // 等待 N 秒
        } catch (Exception ex) {
            logger.debug("waittingRandomTime Exception :\n", ex);
        }
    }

    private int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    
}
