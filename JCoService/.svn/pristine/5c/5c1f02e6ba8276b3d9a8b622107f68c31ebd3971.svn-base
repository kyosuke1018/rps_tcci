/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.facade;

import com.tcci.sap.jco.entity.JcoServiceLog;
import com.tcci.sap.jco.model.RfcProxyRecord;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author peter.pan
 */
@Stateless
public class JcoServiceLogFacade extends AbstractFacade<JcoServiceLog> {
    @PersistenceContext(unitName = "datawarehousePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public JcoServiceLogFacade() {
        super(JcoServiceLog.class);
    }
    
    public JcoServiceLog genJcoServiceLog(RfcProxyRecord rfcProxyRecord){
        JcoServiceLog jcoServiceLog = new JcoServiceLog();
        jcoServiceLog.setClientCode(rfcProxyRecord.getClientCode());
        jcoServiceLog.setClientIp(rfcProxyRecord.getClientIP());
        jcoServiceLog.setFunctionName(rfcProxyRecord.getFunctionName());
        jcoServiceLog.setInputBrief(rfcProxyRecord.getInputBrief());
        jcoServiceLog.setOperator(rfcProxyRecord.getOperator());
        jcoServiceLog.setRunTime(rfcProxyRecord.getRunTime());
        jcoServiceLog.setSapClientCode(rfcProxyRecord.getSapClientcode());
        jcoServiceLog.setServerIp(rfcProxyRecord.getServerIP());
        jcoServiceLog.setSid(rfcProxyRecord.getId());
        jcoServiceLog.setSuccess(rfcProxyRecord.isSuccess());
        jcoServiceLog.setTimeConsuming(rfcProxyRecord.getTimeConsuming());
        
        return jcoServiceLog;
    }
    
    public void save(RfcProxyRecord rfcProxyRecord){
        JcoServiceLog jcoServiceLog = genJcoServiceLog(rfcProxyRecord);
        jcoServiceLog.setCreatetimestamp(new Date());
        
        this.create(jcoServiceLog);
    }
}
