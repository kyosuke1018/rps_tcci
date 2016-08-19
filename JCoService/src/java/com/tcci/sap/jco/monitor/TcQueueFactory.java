package com.tcci.sap.jco.monitor;

import com.tcci.sap.jco.model.EnumQueueType;
import com.tcci.sap.jco.model.QueueContainer;
import com.tcci.sap.jco.model.RfcProxyRecord;
import com.tcci.sap.jco.qualifier.MemoryQueueQualifier;
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 依系統設定傳回要使用的 Buffer Queue
 * @author Peter.pan
 */
@Singleton
public class TcQueueFactory {
    private Logger logger = LoggerFactory.getLogger(TcQueueFactory.class);

    @Inject @MemoryQueueQualifier QueueContainer<RfcProxyRecord> proxyRecordQueue; // Memory Queue
    // @Inject @ActiveMQQualifier QueueContainer<RfcProxyRecord> activeMQContainer; // 目前不使用 Open Queue，先註解減少Server設定

    /**
     * 依類別傳回要使用的 Buffer Queue
     * @param qType
     * @return 
     */
    public QueueContainer<RfcProxyRecord> getQueue(EnumQueueType qType) {
        switch(qType){
            case APACHE_ACTIVEMQ:
            case GLASSFISH_MQ: // 目前不使用 Open Queue，先註解減少Server設定
                /*if( activeMQContainer==null ){
                    logger.debug("getQueue activeMQContainer==null !");
                }
                return activeMQContainer;*/
            default:
                if( proxyRecordQueue==null ){
                    logger.debug("getQueue proxyRecordQueue==null !");
                }                
                return proxyRecordQueue;
        }
    }
}
