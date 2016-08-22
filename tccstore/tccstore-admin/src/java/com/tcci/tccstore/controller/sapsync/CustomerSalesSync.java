/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.sapsync;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.storeadmin.facade.sync.customer.CustomerSyncData;
import com.tcci.storeadmin.facade.sync.customer.CustomerSyncFacade;
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
public class CustomerSalesSync {

    private CustomerSyncData syncData;

    @EJB
    transient private CustomerSyncFacade syncFacade;

    @PostConstruct
    private void init() {
    }

    public void sync() {
        try {
            syncData = syncFacade.sync();
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

    // helper
    public CustomerSyncData getSyncData() {
        return syncData;
    }

    public void setSyncData(CustomerSyncData syncData) {
        this.syncData = syncData;
    }

}
