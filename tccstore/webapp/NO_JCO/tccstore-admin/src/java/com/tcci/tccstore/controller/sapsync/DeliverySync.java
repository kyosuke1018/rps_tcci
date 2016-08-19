/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.sapsync;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.storeadmin.facade.sync.delivery.DeliverySyncData;
import com.tcci.storeadmin.facade.sync.delivery.DeliverySyncFacade;
import com.tcci.storeadmin.facade.sync.delivery.DeliveryVO;
import com.tcci.storeadmin.facade.sync.delivery.DeliveryVkorgVO;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class DeliverySync {

    private boolean fromSap = true; // true:load from SAP, false:load from CSV
    private DeliverySyncData syncData;

    @Resource(mappedName = "jndi/tccstore.config")
    transient private Properties tccstoreConfig;
    
    @EJB
    private DeliverySyncFacade syncFacade;

    @PostConstruct
    private void init() {
        fromSap = "SAP".equals(tccstoreConfig.get("syncFrom"));
    }
    
    public void sync() {
        try {
            syncData = syncFacade.sync(fromSap);
            JsfUtil.addSuccessMessage("資料比對完成!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "sync exception!");
        }
    }

    public void save() {
        if (syncData != null) {
            try {
                syncFacade.save(syncData);
                JsfUtil.addSuccessMessage("資料儲存完成!");
            } catch (Exception ex) {
                JsfUtil.addErrorMessage(ex, "save exception!");
            }
        } else {
            JsfUtil.addErrorMessage("請先執行比對!");
        }
    }

    public List<DeliveryVO> getResult() {
        return null == syncData ? null : syncData.getResult1();
    }

    public List<DeliveryVkorgVO> getResult2() {
        return null == syncData ? null : syncData.getResult2();
    }

    //
    public boolean isFromSap() {
        return fromSap;
    }

    public void setFromSap(boolean fromSap) {
        this.fromSap = fromSap;
    }

    public DeliverySyncData getSyncData() {
        return syncData;
    }

    public void setSyncData(DeliverySyncData syncData) {
        this.syncData = syncData;
    }

    public DeliverySyncFacade getSyncFacade() {
        return syncFacade;
    }

    public void setSyncFacade(DeliverySyncFacade syncFacade) {
        this.syncFacade = syncFacade;
    }

}
