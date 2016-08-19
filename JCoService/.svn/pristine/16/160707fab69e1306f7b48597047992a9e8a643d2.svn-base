/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.monitor;

import com.tcci.sap.jco.conf.GlobalConstant;
import com.tcci.sap.jco.facade.JcoServiceLogFacade;
import com.tcci.sap.jco.model.QueueContainer;
import com.tcci.sap.jco.model.QueueProducer;
import com.tcci.sap.jco.model.RfcProxyInput;
import com.tcci.sap.jco.model.RfcProxyRecord;
import com.tcci.sap.jco.util.NetworkUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@Singleton
public class ProxyLogHandler implements QueueProducer<RfcProxyRecord> {
    private final Logger logger = LoggerFactory.getLogger(ProxyLogHandler.class);

    @Inject TcQueueFactory tcQueueFactory;
    @EJB JcoServiceLogFacade jcoServiceLogFacade;
    
    /**
     * 非同步方式執行
     * log request to queue
     * @param request
     * @param inputJsonStr
     * @param input
     * @param timeConsuming
     * @param success 
     * @param msg 
     */
    @Asynchronous
    @Lock
    @AccessTimeout(-1)
    public void handleRequest(HttpServletRequest request, String inputJsonStr, RfcProxyInput input, long timeConsuming, boolean success, String msg){
        logger.debug("handleRequest ...");
        RfcProxyRecord record = new RfcProxyRecord();
        try{
            if( request!=null ){
                record.setClientIP(getClientIP(request));
            }
            if( inputJsonStr!=null && input!=null ){
                record.setClientCode(input.getClientCode());
                record.setInputBrief(inputJsonStr.substring(0, 50));// 避免過多Input浪費記憶體，簡述即可
                record.setFunctionName(input.getFunctionName());
                record.setSapClientcode(input.getSapClientCode());
                record.setOperator(input.getOperator());
            }
            
            record.setSuccess(success);
            
            record.setId(System.currentTimeMillis());
            record.setServerIP(NetworkUtil.getHostIP());
            record.setRunTime(new Date());
            record.setTimeConsuming(timeConsuming);
            
            QueueContainer<RfcProxyRecord> queue = getContainer();
            if( queue!=null ){
                produce(record);// 放入 Queue for admin console
                
                if( !success ){
                    // 失敗顯示詳細輸入內容
                    logger.info("handleRequest inputJsonStr =\n"+inputJsonStr);
                    NotifyHandler.notifyRfcFail(record, inputJsonStr, msg);
                }else{
                    // 判斷是否寄發RFC執行過久警示通知
                    if( timeConsuming>GlobalConstant.MIN_RUN_SLOW_TIME ){
                        logger.debug("RFC run slow alert ...");
                        NotifyHandler.notifyRfcSlow(record);
                    }
                }
            }else{
                logger.error("handleRequest queue = null !!!");
            }
        }catch(Exception e){
            // LOG 勿影響正常流程
            logger.error("handleRequest exception:\n", e);
        }finally{
            // 20160717 log to DW
            if( GlobalConstant.LOG_TO_DB ){
                logToDB(record);
            }
        }
    }
    
    /**
     * 
     * @param record 
     */
    public void logToDB(RfcProxyRecord record){
        if( record==null ){
            logger.error("logToDB error record==null ");
        }
        try{
            jcoServiceLogFacade.save(record);
        }catch(Exception e){
            logger.error("logToDB Exception :\n", e);
        }
    }

    /**
     * return exception Stack Trace
     * @param ex
     * @return 
     */
    public String getErrorStackTrace(Throwable ex){
        try{
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            
            return errors.toString();
        }catch(Exception e){
            logger.error("getErrorStackTrace Exception:\n{}", e);
        }
        return null;
    }
    
    /**
     * 取得 Request ClientIP (考慮Proxy)
     * @param request
     * @return 
     */
    public String getClientIP(HttpServletRequest request){
        String clientIp = null;
        try{
            clientIp = request.getHeader("X-Forwarded-For");
            if (null == clientIp) {
                clientIp = request.getRemoteAddr();
            }
        }catch(Exception e){
            logger.error("getClientIP exception \n:", e);
        }
        return clientIp;
    }
    
    @Override
    public QueueContainer<RfcProxyRecord> getContainer() {
        return tcQueueFactory.getQueue(GlobalConstant.getQueueType());
    }

    @Override
    public boolean produce(RfcProxyRecord entry) {
        if( !entry.isSuccess() ){// 失敗額外保存至 error queue
            getContainer().enqueue(GlobalConstant.getErrQueueName(), entry);
        }
        return getContainer().enqueue(GlobalConstant.getDefQueueName(), entry);
    }
    
}
