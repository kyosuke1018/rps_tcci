/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.controller;

import com.sap.conn.jco.JCoDestination;
import com.tcci.fc.util.sap.jco.JCoConnection;
import com.tcci.sap.jco.conf.GlobalConstant;
import com.tcci.sap.jco.conf.JCoConfigManager;
import com.tcci.sap.jco.model.IndexQueryCondVO;
import com.tcci.sap.jco.model.QueueContainer;
import com.tcci.sap.jco.model.RfcProxyRecord;
import com.tcci.sap.jco.monitor.TcQueueFactory;
import com.tcci.sap.jco.util.JsfUtils;
import com.tcci.sap.jco.util.NetworkUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@ManagedBean(name = "indexController")
@ViewScoped
public class IndexController implements Serializable {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Inject TcQueueFactory tcQueueFactory;
    
    private Date entryTime;
    private IndexQueryCondVO condVO;
    
    @PostConstruct
    protected void init() {
        logger.debug("ConfigController init ...");
        initQueryCond();
        refresh();
    }
 
    private void initQueryCond(){
        condVO = new IndexQueryCondVO();
        condVO.setQueueName(GlobalConstant.getDefQueueName());
    }
    
    public String getServerIp(){
       return NetworkUtil.getHostIP();
    }
    
    public List<String> getSapClientCodes(){
        // List<String> clients = JCoConfigManager.getSapClientCodes();
        // if( clients==null ){ clients = new ArrayList<String>(); }
        List<String> clients = new ArrayList<String>();
        Map<String, String> clientMap = JCoConfigManager.getJcoClientMap();
        
        if( clientMap!=null ){
            for(String key : clientMap.keySet()){
                clients.add(key + " ("  + clientMap.get(key) + ")");
            }
        }
        
        logger.debug("getSapClientCodes clients.size = "+clients.size());
        
        return clients;
    }
    
    public List<SelectItem> getPropsBySapClientCode(String sapClientCode){
        logger.info("getPropsBySapClientCode sapClientCode = "+sapClientCode);
        List<SelectItem> list = new ArrayList<SelectItem>();
        try{
            logger.debug("JCoConfigManager.getClientDestMap().get(sapClientCode) = "+JCoConfigManager.getClientDestMap().get(sapClientCode));
            for(String key : JCoConfigManager.getDestinationPropsMap().get(JCoConfigManager.getClientDestMap().get(sapClientCode)).stringPropertyNames() ){
                String value = JCoConfigManager.getDestinationPropsMap().get(JCoConfigManager.getClientDestMap().get(sapClientCode)).getProperty(key);
                list.add(new SelectItem(value, key));
            }
        }catch(Exception e){
            logger.error("getPropsBySapClientCode exception:\n", e);
        }
        return list;
    }
    
    public void refresh(){
        logger.info("refresh ...");
        entryTime = new Date();
    }
    
    /**
     * 執行 JCoDestination clear RFC mata cache 
     */
    public void clearRepository(){
        logger.debug("clearRepository ...");
        StringBuilder sbS = new StringBuilder();
        StringBuilder sbE = new StringBuilder();
        
        for(String clientCode : getSapClientCodes() ){
            try{
                JCoDestination jcoDestination = JCoConfigManager.getJCoDestinationByClient(clientCode);
                JCoConnection jcoConnection = new JCoConnection();
                jcoConnection.clearRepository(jcoDestination);

                sbS.append("[").append(clientCode).append("]");
                logger.info("clearRepository ... "+clientCode+" completed.");
            }catch(Exception e){
                sbE.append("[").append(clientCode).append("]");
                logger.info("clearRepository ... "+clientCode+" fail...\n", e);
            }
        }
        
        if( sbS.toString().isEmpty() ){
            JsfUtils.addSuccessMessage("成功：" + sbS.toString());
        }
        if( sbE.toString().isEmpty() ){
            JsfUtils.addErrorMessage("失敗：" + sbE.toString());
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Queue Info">
    public void changeQueue(){
        logger.debug("changeQueue ..." + condVO.getQueueName());
    }
    
    /**
     * 取得 Quene 狀態 
     * @param condVO
     * @return 
     */    
    public List<RfcProxyRecord> getQueueInfo(IndexQueryCondVO condVO){
        List<RfcProxyRecord> list = null;

        if( tcQueueFactory!=null ){
            QueueContainer<RfcProxyRecord> queue = tcQueueFactory.getQueue(GlobalConstant.getQueueType());
            list = queue.getAll(condVO.getQueueName());
        }
        if( list==null ){
            return new ArrayList<RfcProxyRecord>();// 前端不檢查 NULL
        }
            
        if( condVO.getKeywork()!=null && !condVO.getKeywork().trim().isEmpty() ){
            List<RfcProxyRecord> retList = new ArrayList<RfcProxyRecord>();
            for(RfcProxyRecord vo : list){
                // 關鍵字查詢
                if( (vo.getClientCode()!=null && vo.getClientCode().toUpperCase().contains(condVO.getKeywork().trim().toUpperCase())) 
                    || (vo.getFunctionName()!=null && vo.getFunctionName().toUpperCase().contains(condVO.getKeywork().trim().toUpperCase())) 
                    || (vo.getOperator()!=null && vo.getOperator().toUpperCase().contains(condVO.getKeywork().trim().toUpperCase())) 
                    || (vo.getClientIP()!=null && vo.getClientIP().toUpperCase().contains(condVO.getKeywork().trim().toUpperCase()))
                    || (vo.getSapClientcode()!=null && vo.getSapClientcode().toUpperCase().contains(condVO.getKeywork().trim().toUpperCase())) 
                ){
                    retList.add(vo);
                }
            }

            list = retList;
        }
        
        // 反序顯示
        Collections.sort(list);
        return list;
    }
    
    public String getDefQueueName(){
        return GlobalConstant.getDefQueueName();
    }
    public String getErrQueueName(){
        return GlobalConstant.getErrQueueName();
    }
    public List<RfcProxyRecord> getQueueInfo(){
        return getQueueInfo(condVO);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public IndexQueryCondVO getCondVO() {
        return condVO;
    }

    public void setCondVO(IndexQueryCondVO condVO) {
        this.condVO = condVO;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }
    //</editor-fold>
}
