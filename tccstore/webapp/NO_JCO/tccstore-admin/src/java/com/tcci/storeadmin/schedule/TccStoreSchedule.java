/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.schedule;

import com.tcci.fc.facade.schedule.TcScheduleFacade;
import com.tcci.fc.util.VelocityMail;
import com.tcci.storeadmin.facade.sync.contract.ContractSyncData;
import com.tcci.storeadmin.facade.sync.contract.ContractSyncFacade;
import com.tcci.storeadmin.facade.sync.delivery.DeliverySyncData;
import com.tcci.storeadmin.facade.sync.delivery.DeliverySyncFacade;
import java.util.HashMap;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Stateless
public class TccStoreSchedule {

    Logger logger = LoggerFactory.getLogger(TccStoreSchedule.class);
    
    @EJB
    private TcScheduleFacade scheduleFacade;
    @EJB
    private OrderJobs orderJobs;
    @EJB
    private RewardJobs rewardJobs;
    @EJB
    private DeliverySyncFacade deliverySyncFacade;
    @EJB
    private ContractSyncFacade contractSyncFacade;

    @Resource(mappedName = "jndi/tccstore.config")
    transient private Properties tccstoreConfig;
    
    // 已核准的訂單與SAP同步
    // APPROVE -> CLOSE，如果SAP的訂單狀態是已出貨
    @Schedule(hour = "*", minute = "10", persistent = false)
    public void dailySyncOrderStatus() {
        try {
            // 3分內不要再執行
            if (scheduleFacade.canExecute("dailySyncOrderStatus", 3)) {
                orderJobs.syncOrderStatus();
            } else {
                logger.warn("dailySyncOrderStatus not execute");
            }
        } catch (Exception ex) {
            logger.error("dailySyncOrderStatus exception", ex);
        }
    }

    // 取消已過期的訂單
    @Schedule(hour = "12", minute = "30", persistent = false)
    public void cancelExpiredOrder() {
        try {
            // 3分內不要再執行
            if (scheduleFacade.canExecute("cancelExpiredOrder", 3)) {
                orderJobs.cancelExpiredOrder();
            } else {
                logger.warn("cancelExpiredOrder not execute");
            }
        } catch (Exception ex) {
            logger.error("cancelExpiredOrder exception", ex);
        }
    }

    // 訂單建立(OPEN)批次通知
    @Schedule(hour = "*", minute = "5,35", persistent = false)
    public void openOrdersNotify() {
        try {
            // 3分內不要再執行
            if (scheduleFacade.canExecute("openOrdersNotify", 3)) {
                orderJobs.openOrdersNotify();
            } else {
                logger.warn("openOrdersNotify not execute");
            }
        } catch (Exception ex) {
            logger.error("openOrdersNotify exception", ex);
        }
    }

    // 自動審核訂單
    // @Schedule(hour = "*", minute = "*/5", persistent = false)
    public void autoOrderApprove() {
        try {
            // 3分內不要再執行
            if (scheduleFacade.canExecute("autoOrderApprove", 3)) {
                orderJobs.autoOrderApprove();
            } else {
                logger.warn("autoOrderApprove not execute");
            }
        } catch (Exception ex) {
            logger.error("autoOrderApprove exception", ex);
        }
    }
    
    // 每日登入贈送金幣1點
    @Schedule(hour = "0", minute = "5", persistent = false)
    public void dailyLoginReward() {
        try {
            // 3分內不要再執行
            if (scheduleFacade.canExecute("dailyLoginReward", 3)) {
                rewardJobs.loginReward();
            } else {
                logger.warn("dailyLoginReward not execute");
            }
        } catch (Exception ex) {
            logger.error("dailyLoginReward exception", ex);
        }
    }

    // 每日送達地點同步
    @Schedule(hour = "8", minute = "45", persistent = false)
    public void dailyDeliveryPlaceSync() {
        boolean execute = false;
        DeliverySyncData syncData = null;
        String syncError = "";
        try {
            // 3分內不要再執行
            if (scheduleFacade.canExecute("dailyDeliveryPlaceSync", 3)) {
                execute = true;
                boolean fromSap = "SAP".equals(tccstoreConfig.get("syncFrom"));
                syncData = deliverySyncFacade.sync(fromSap);
                deliverySyncFacade.save(syncData);
            } else {
                logger.warn("dailyDeliveryPlaceSync not execute");
            }
        } catch (Exception ex) {
            logger.error("dailyDeliveryPlaceSync exception", ex);
            if (ex instanceof EJBException) {
                ex = getRootCause((EJBException) ex);
            }
            syncError = "错误原因:" + ex.getMessage();
        }
        // 如果有執行，寄送同步結果
        if (execute) {
            HashMap<String, Object> mailBean = new HashMap<>();
            String subject = "[台泥电商] 送达地点同步结果";
            mailBean.put(VelocityMail.SUBJECT, subject);
            mailBean.put(VelocityMail.TO, "jimmy.lee@tcci.com.tw");
            mailBean.put("syncData", syncData);
            mailBean.put("syncError", syncError);
            VelocityMail.sendMail(mailBean, "/mail_deliverySyncNotify.vm");
        }
    }

    // 每日合約同步
    @Schedule(hour = "8", minute = "45", persistent = false)
    public void dailyContractSync() {
        boolean execute = false;
        ContractSyncData syncData = null;
        String syncError = "";
        try {
            // 3分內不要再執行
            if (scheduleFacade.canExecute("dailyContractSync", 3)) {
                execute = true;
                boolean fromSap = "SAP".equals(tccstoreConfig.get("syncFrom"));
                syncData = contractSyncFacade.sync(fromSap);
                contractSyncFacade.save(syncData);
            } else {
                logger.warn("dailyContractSync not execute");
            }
        } catch (Exception ex) {
            logger.error("dailyContractSync exception", ex);
            if (ex instanceof EJBException) {
                ex = getRootCause((EJBException) ex);
            }
            syncError = "错误原因:" + ex.getMessage();
        }
        // 如果有執行，寄送同步結果
        if (execute) {
            HashMap<String, Object> mailBean = new HashMap<>();
            String subject = "[台泥电商] 合约同步结果";
            mailBean.put(VelocityMail.SUBJECT, subject);
            mailBean.put(VelocityMail.TO, "jimmy.lee@tcci.com.tw");
            mailBean.put("syncData", syncData);
            mailBean.put("syncError", syncError);
            VelocityMail.sendMail(mailBean, "/mail_contractSyncNotify.vm");
        }
    }

    // 批次同步SAP開單/取消結果
    @Schedule(hour = "*", minute = "10", persistent = false)
    public void zstdSoinputSync() {
        try {
            // 3分內不要再執行
            if (scheduleFacade.canExecute("zstdSoinputSync", 3)) {
                orderJobs.zstdSoinputSync();
            } else {
                logger.warn("zstdSoinputSync not execute");
            }
        } catch (Exception ex) {
            logger.error("zstdSoinputSync exception", ex);
        }
    }
    
    // helper
    public static Exception getRootCause(EJBException exception) {
        if (null==exception ) {
            return null;
        }

        EJBException effect = exception;
        Exception cause = effect.getCausedByException();

        while ( null != cause  &&  cause instanceof EJBException ) {
            effect = (EJBException) cause;
            cause = effect.getCausedByException();
        }

        return null == cause ? effect : cause;
    }  

}
