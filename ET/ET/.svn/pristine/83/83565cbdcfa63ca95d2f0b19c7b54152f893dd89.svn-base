/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.schedule;

import com.tcci.et.facade.EtMemberFacade;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpm.IBPMEngine;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.facade.schedule.TcScheduleFacade;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EtScheduleFacade {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Inject
    private TcScheduleFacade scheduleFacade;
    @Inject
    private TcUserFacade userFacade;
    @Inject
    private IBPMEngine bpmEngine;
    @EJB private EtTenderFacade etTenderFacade;
    @EJB private EtMemberFacade etMemberFacade;
    
    
//    @Schedule(hour = "10", minute = "18", persistent = false)
//    @Schedule(hour = "*", minute = "21", persistent = false)
    @Schedule(hour = "3", minute = "0", persistent = false)
    public void updateTenderStatus() {
        try {
            // 30分內不要再執行
            if (scheduleFacade.canExecute("updateTenderStatus", 30)) {
//            if (scheduleFacade.canExecute("updateTenderStatus", 20)) {
                logger.debug("updateTenderStatus() begin");
//                TcUser admin = userFacade.findUserByLoginAccount("administrator", Boolean.FALSE);
                TcUser admin = userFacade.findUserByLoginAccount("administrator", null);
//                logger.debug("admin={}", admin!=null?admin.getId():"not found");
                etTenderFacade.batchUpdateTenderStatus(admin);
                logger.debug("updateTenderStatus() end");
            } else {
                logger.warn("updateTenderStatus not execute");
            }
        } catch (Exception ex) {
            logger.error("updateTenderStatus exception", ex);
        }
    }
    
    @Schedule(hour = "3", minute = "30", persistent = false)
    public void checkBlockVender() {
        try {
            // 30分內不要再執行
            if (scheduleFacade.canExecute("checkBlockVender", 30)) {
//            if (scheduleFacade.canExecute("checkBlockVender", 10)) {
                logger.debug("checkBlockVender() begin");
                TcUser admin = userFacade.findUserByLoginAccount("administrator", null);
                etMemberFacade.batchCheckBlockVender(admin);
                logger.debug("checkBlockVender() end");
            } else {
                logger.warn("checkBlockVender not execute");
            }
        } catch (Exception ex) {
            logger.error("checkBlockVender exception", ex);
        }
    }
    
    @Schedule(hour = "4", minute = "0", persistent = false)
    public void publishNotification() {
        try {
            // 30分內不要再執行
            if (scheduleFacade.canExecute("publishNotification", 30)) {
                logger.debug("publishNotification() begin");
                TcUser admin = userFacade.findUserByLoginAccount("administrator", null);
                etTenderFacade.batchPublishNotification(admin);
                logger.debug("publishNotification() end");
            } else {
                logger.warn("publishNotification not execute");
            }
        } catch (Exception ex) {
            logger.error("publishNotification exception", ex);
        }
    }
    
    // 待簽核清單批次通知
    // 週一至週五早上6點
//    @Schedule(dayOfWeek = "Mon,Tue,Wed,Thu,Fri", hour = "6", minute = "0", persistent = false)
    @Schedule(hour = "6", minute = "0", persistent = false)
    public void notifySignWaitingBatch() {
        logger.info("notifySignWaitingBatch begin");
        try {
            bpmEngine.batchNotifyRunningWorkitems();
        } catch (Exception ex) {
            logger.error("notifyRunningWorkitems", ex);
        }
    }
    
}
