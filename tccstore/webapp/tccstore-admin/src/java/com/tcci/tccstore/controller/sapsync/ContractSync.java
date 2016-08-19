/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.sapsync;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.storeadmin.facade.sync.contract.ContractSyncData;
import com.tcci.storeadmin.facade.sync.contract.ContractSyncFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class ContractSync {

    private boolean fromSap = false; // true:load from SAP, false:load from CSV
    private ContractSyncData syncData;

    //@Resource(mappedName = "jndi/tccstore.config")
    //transient private Properties tccstoreConfig;
    @EJB
    private ContractSyncFacade syncFacade;

    @PostConstruct
    private void init() {
        // fromSap = "SAP".equals(tccstoreConfig.get("syncFrom"));
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
    
    public void sapContract2Csv() {
        try {
            syncFacade.sapContract2Csv();
            JsfUtil.addSuccessMessage("sapContract2Csv success!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "exception!");
        }
    }

    public List<com.tcci.storeadmin.facade.sync.contract.ContractVO> getResult1() {
        return null == syncData ? null : syncData.getResult1();
    }

    public List<com.tcci.storeadmin.facade.sync.contract.ContractProductVO> getResult2() {
        return null == syncData ? null : syncData.getResult2();
    }

    // getter, setter
    public boolean isFromSap() {
        return fromSap;
    }

    public void setFromSap(boolean fromSap) {
        this.fromSap = fromSap;
    }

    public ContractSyncData getSyncData() {
        return syncData;
    }

    public void setSyncData(ContractSyncData syncData) {
        this.syncData = syncData;
    }

}
