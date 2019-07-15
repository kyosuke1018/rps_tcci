/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.admin;

import static com.tcci.cm.controller.admin.SuperUserController.FUNC_OPTION;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.enums.OptionEnum;
import com.tcci.et.facade.jco.JCoClientFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author peter.pan
 */
@ManagedBean(name = "sap")
@ViewScoped
public class SapSyncController extends SessionAwareController implements Serializable {
    private static final long FUNC_OPTION = 15;
    
    @EJB JCoClientFacade jco;
    
    List<SelectItem> clientOps = new ArrayList<SelectItem>();
    List<SelectItem> werksOps = new ArrayList<SelectItem>();
    List<SelectItem> bukrsOps = new ArrayList<SelectItem>();
    List<SelectItem> sprasOps = new ArrayList<SelectItem>();
    
    Map<String, String> clientMap = new HashMap<String, String>();
    Map<String, String> werksMap = new HashMap<String, String>();
    Map<String, String> bukrsMap = new HashMap<String, String>();
    Map<String, String> sprasMap = new HashMap<String, String>();
    
    @PostConstruct
    private void init(){
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // Get view Id
        viewId = JsfUtils.getViewId();
        logger.debug("SapSyncController viewId = "+viewId);
    }
    
    /**
     * Sync Sap Data To DB
     * @param code 
     */
    public void syncSapData(String code){
        logger.debug("syncSapData code = "+code);
        String client = clientMap.get(code);
        String werks = werksMap.get(code);
        String bukrs = bukrsMap.get(code);
        String spras = sprasMap.get(code);
        logger.debug("syncSapData client = "+client+", werks = "+werks+", bukrs = "+bukrs+", spras = "+spras);
        
        if( "T024".equals(code) ){// 採購群組
            jco.syncPurGroup(client, werks, this.getLoginUser());
        }else if( "T024E".equals(code) ){// 採購組織
            jco.syncPurOrg(client, bukrs, this.getLoginUser());
        }
    }
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }  
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public List<SelectItem> getClientOps() {
        return clientOps;
    }

    public void setClientOps(List<SelectItem> clientOps) {
        this.clientOps = clientOps;
    }

    public List<SelectItem> getWerksOps() {
        return werksOps;
    }

    public void setWerksOps(List<SelectItem> werksOps) {
        this.werksOps = werksOps;
    }

    public List<SelectItem> getBukrsOps() {
        return bukrsOps;
    }

    public void setBukrsOps(List<SelectItem> bukrsOps) {
        this.bukrsOps = bukrsOps;
    }

    public List<SelectItem> getSprasOps() {
        return sprasOps;
    }

    public void setSprasOps(List<SelectItem> sprasOps) {
        this.sprasOps = sprasOps;
    }

    public Map<String, String> getClientMap() {
        return clientMap;
    }

    public void setClientMap(Map<String, String> clientMap) {
        this.clientMap = clientMap;
    }

    public Map<String, String> getWerksMap() {
        return werksMap;
    }

    public void setWerksMap(Map<String, String> werksMap) {
        this.werksMap = werksMap;
    }

    public Map<String, String> getBukrsMap() {
        return bukrsMap;
    }

    public void setBukrsMap(Map<String, String> bukrsMap) {
        this.bukrsMap = bukrsMap;
    }

    public Map<String, String> getSprasMap() {
        return sprasMap;
    }

    public void setSprasMap(Map<String, String> sprasMap) {
        this.sprasMap = sprasMap;
    }
    //</editor-fold>
}