/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.monitor;

import com.tcci.sap.jco.conf.GlobalConstant;
import com.tcci.sap.jco.model.RfcProxyRecord;
import com.tcci.sap.jco.util.DateUtil;
import com.tcci.sap.jco.util.MailUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Email 通知程式
 * 
 * @author Peter.pan
 */
public class NotifyHandler {
    private final static Logger logger = LoggerFactory.getLogger(NotifyHandler.class);
    
    private static String commonNotifyInfo(RfcProxyRecord record){
        StringBuilder content = new StringBuilder()
                            .append("ID： ").append(record.getId()).append("<br/>\n")
                            .append("Time： ").append(DateUtil.format(record.getRunTime())).append("<br/>\n")
                            .append("AP： ").append(record.getClientCode()).append("<br/>\n")
                            .append("IP： ").append(record.getClientIP()).append("<br/>\n")
                            .append("Operator： ").append(record.getOperator()).append("<br/>\n")
                            .append("RFC： ").append(record.getFunctionName()).append("<br/>\n")
                            .append("Client Code： ").append(record.getSapClientcode()).append("<br/>\n")
                            .append("JCoServer Server： ").append(record.getServerIP()).append("<br/>\n");
        
        return content.toString();
    }
    
    /**
     * 代理執行 RFC 時間過長通知
     * @param record
     * @return 
     */
    public static boolean notifyRfcSlow(RfcProxyRecord record){
        try{
            if( record!=null ){
                StringBuilder subject = new StringBuilder().append(GlobalConstant.NOTIFY_SLOW_SUBJECT);
                StringBuilder content = new StringBuilder().append(commonNotifyInfo(record));
                content.append("耗時： ").append(record.getTimeConsuming()).append("ms <br/>\n");
                content.append("請檢視 RFC 程式有無改善空間。 <br/>\n");
                
                return notifyByEMail(subject.toString(), content.toString());
            }
        }catch(Exception e){
            logger.error("notifyRfcFail exception:\n", e);
        }
        return false;
    }
    
    /**
     * 代理執行 RFC 錯誤通知
     * @param record
     * @param inputJsonStr
     * @param errMsg
     * @return 
     */
    public static boolean notifyRfcFail(RfcProxyRecord record, String inputJsonStr, String errMsg){
        try{
            if( record!=null ){
                StringBuilder subject = new StringBuilder().append(GlobalConstant.NOTIFY_FAIL_SUBJECT);
                StringBuilder content = new StringBuilder().append(commonNotifyInfo(record));
                // 錯誤顯示詳細輸入
                if( inputJsonStr!=null ){
                    content.append("Input JSON： <br/>\n<pre>").append(inputJsonStr).append("</pre><br/>\n");
                }
                // 錯誤訊息
                if( errMsg!=null ){
                    content.append("Exception Stack Trace： <br/>\n<pre>").append(errMsg).append("</pre><br/>\n");
                }
                return notifyByEMail(subject.toString(), content.toString());
            }
        }catch(Exception e){
            logger.error("notifyRfcFail exception:\n", e);
        }
        return false;
    }
    
    /**
     * 自訂內容通知
     * @param subject
     * @param content
     * @return 
     */
    public static boolean notifyByEMail(String subject, String content){
        try{
            boolean enabled = GlobalConstant.NOTIFY_ENABLED;
            String mailTo = null;
            Properties jndiProperties = loadJndiConfig();
            if( jndiProperties!=null ){
                enabled = Boolean.valueOf(jndiProperties.getProperty(GlobalConstant.JNDI_NOTIFY_ENABLED, mailTo));
                mailTo = jndiProperties.getProperty(GlobalConstant.JNDI_NOTIFY_EMAIL_TO, mailTo);
            }
            if( enabled && mailTo!=null && !mailTo.isEmpty() ){
                subject = GlobalConstant.NOTIFY_EMAIL_SUBJECT+" - "+subject;
                Map<String, String> params = new HashMap<String, String>();
                params.put("TITLE", subject);
                params.put("DETAIL", content);

                MailUtil.sendMail(subject, mailTo, params);
            }
        }catch(Exception e){
            logger.error("notifyByEMail exception \n:", e);
            return false;
        }
        
        return true;
    }
    
    public static Properties loadJndiConfig(){
        try{
            Context ctx = new InitialContext();
            // JCO JNDI 非必要；若無則使用 FILE 載入內容
            Properties jndiProperties = (Properties)ctx.lookup(GlobalConstant.JNDI_JCOSERVICE);
            return jndiProperties;
        }catch(NamingException e){
            logger.error("loadJndiConfig for "+ GlobalConstant.JNDI_JCOSERVICE +" Exception:\n", e);
        }
        return null;
    }
}
