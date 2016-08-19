/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.monitor;

import com.tcci.sap.jco.conf.GlobalConstant;
import com.tcci.sap.jco.model.QueueContainer;
import com.tcci.sap.jco.model.RfcProxyRecord;
import com.tcci.sap.jco.qualifier.MemoryQueueQualifier;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@Singleton
@MemoryQueueQualifier
public class LogMemoryQueue implements QueueContainer<RfcProxyRecord>, Serializable {
    private final Logger logger = LoggerFactory.getLogger(LogMemoryQueue.class);  

    // Queue Map
    private Map<String, BlockingQueue<RfcProxyRecord>> rfcQueueMap;
    int maxQueueSize = 0;
    
    /**
     * 初始設定
     */
    public LogMemoryQueue(){
        logger.info("LogMemoryQueue construtor ...");
        init();
    }
    
    private void init(){
        // 目前只需2個Queue
        List<String> queueNameList = new ArrayList<String>();
        queueNameList.add(GlobalConstant.getDefQueueName());// 共用 log
        queueNameList.add(GlobalConstant.getErrQueueName());// 錯誤 log
        queueNameList.add(GlobalConstant.getDbQueueName());// save db log
        
        init(queueNameList, GlobalConstant.getQueueSize());
    }
    
    /**
     * 建立多個 Queue，設定 max size
     * @param queueNameList
     * @param maxQueueSize 
     */
    public void init(List<String> queueNameList, int maxQueueSize){
        this.maxQueueSize = maxQueueSize;
        
        if( rfcQueueMap==null ){
            rfcQueueMap = new HashMap<String, BlockingQueue<RfcProxyRecord>>();
        }
        
        for(String queueName : queueNameList){
            if( rfcQueueMap.get(queueName)==null ){
                LinkedBlockingQueue<RfcProxyRecord> rfcQueue = new LinkedBlockingQueue<RfcProxyRecord>(maxQueueSize);
                rfcQueueMap.put(queueName, rfcQueue);
            }
        }
    }
    
    /**
     * 加入工作項目
     * @param queueName
     * @param rfcProxyRecord
     * @return 
     */
    @Override
    public boolean enqueue(String queueName, RfcProxyRecord rfcProxyRecord) {       
        boolean res = add(queueName, rfcProxyRecord);
        return res;
    }

    /**
     * @param queueName
     * @return 
     */
    @Override
    public RfcProxyRecord dequque(String queueName) {
        RfcProxyRecord rfcProxyRecord = rfcQueueMap.get(queueName).poll();
        
        return rfcProxyRecord;
    }

    @Override
    public int getSize(String queueName) {
        return rfcQueueMap.get(queueName).size();
    }
    
    /**
     * 新增 Queue 項目
     * @param queueName
     * @param rfcProxyRecord
     * @return 
     */
    public boolean add(String queueName, RfcProxyRecord rfcProxyRecord){
        BlockingQueue<RfcProxyRecord> rfcQueue = rfcQueueMap.get(queueName);
        if( rfcQueue!=null ){
            if( rfcQueue.size()>=maxQueueSize ){
                RfcProxyRecord item = rfcQueue.remove(); // 超過最大容量，捨棄最舊的 Item
                logger.info("add => Queue.size()>=maxQueueSize : remove (" + item +") !");
            }

            return rfcQueue.add(rfcProxyRecord);
        }
        return false;
    }
    
    /**
     * 從保存檔案回覆 Queue 內容
     * @param queueName
     * @param rfcProxyRecord
     * @return 
     */
    @Override
    public int restore(String queueName, RfcProxyRecord rfcProxyRecord){
        int count = 0;
        try {
            if( add(queueName, rfcProxyRecord) ){
                count++;
            }
        }catch(Exception e){
            logger.error("restore Exception : \n", e);
        }
        
        return count;
    }

    @Override
    public List<RfcProxyRecord> getAll(String queueName) {
        List<RfcProxyRecord> list = null; 
        BlockingQueue<RfcProxyRecord> rfcQueue = rfcQueueMap.get(queueName);
        if( rfcQueue!=null ){
            RfcProxyRecord[] record = rfcQueue.toArray(new RfcProxyRecord[0]);
            if( record!=null ){
                list = Arrays.asList(record);
            }
        }
        return list;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public Map<String, BlockingQueue<RfcProxyRecord>> getJcoFunctionQueueMap() {
        return rfcQueueMap;
    }

    public void setJcoFunctionQueueMap(Map<String, BlockingQueue<RfcProxyRecord>> rfcQueueMap) {
        this.rfcQueueMap = rfcQueueMap;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }
    //</editor-fold>

}
