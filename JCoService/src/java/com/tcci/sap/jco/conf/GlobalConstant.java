/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.conf;

import com.tcci.sap.jco.model.EnumQueueType;

/**
 *
 * @author Peter.pan
 */
public class GlobalConstant {
    // log to DB
    public final static boolean LOG_TO_DB = true;
    // for email notify
    public final static String NOTIFY_EMAIL_SUBJECT = "[JCoService]";
    public final static String NOTIFY_FAIL_SUBJECT = "代理執行 SAP RFC 失敗";
    public final static String NOTIFY_SLOW_SUBJECT = "代理執行 SAP RFC 時間過長警示";
    public final static String JNDI_MAIL_SESSION = "mail/automail";
    public final static String JNDI_JCOSERVICE = "jndi/jcoservice.config";
    
    public final static String JNDI_NOTIFY_PREFIX = "notify.";
    public final static String JNDI_NOTIFY_ENABLED = "notify.email.enabled";
    public final static String JNDI_NOTIFY_EMAIL_TO = "notify.email.to";
    public final static String JNDI_NOTIFY_RUN_SLOW = "notify.email.runSlow";
    public final static boolean NOTIFY_ENABLED = true;
    public final static int MIN_RUN_SLOW_TIME = 30000; // 30s 判斷是否寄發RFC執行過久警示通知

    public final static int QUEUE_SIZE = 100;
    public final static String QUEUE_NAME_DEF = "JCoService.Queue.1";
    public final static String QUEUE_NAME_ERR = "JCoService.Queue.E1";
    public final static String QUEUE_NAME_DB = "JCoService.Queue.DB1";
    
    private static final EnumQueueType queueType = EnumQueueType.MEMORY_QUEUE;
    private static final String defQueueName = QUEUE_NAME_DEF;
    private static final String errQueueName = QUEUE_NAME_ERR;
    private static final String dbQueueName = QUEUE_NAME_DB;
    private static final int queueSize = QUEUE_SIZE;

    public static EnumQueueType getQueueType() {
        return queueType;
    }

    public static String getDefQueueName() {
        return defQueueName;
    }

    public static String getErrQueueName() {
        return errQueueName;
    }

    public static String getDbQueueName() {
        return dbQueueName;
    }

    public static int getQueueSize() {
        return queueSize;
    }
}
